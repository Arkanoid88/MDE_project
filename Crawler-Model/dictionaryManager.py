import pickle
from github import Github
from gitcloner import commits, repository, users


def repository_add(repository, user, file):
    repo_dict = {}
    repo_dict['name'] = repository.repo_name()  # str
    repo_dict['stargazers'] = repository.repo_stargazers()  # int
    repo_dict['watchers'] = repository.repo_watchers_count()  # int
    repo_dict['forks'] = repository.repo_forks_count()  # int
    repo_dict['fork'] = repository.repo_forks()  # bool
    repo_dict['size'] = repository.repo_size()  # int
    repo_dict['owner'] = user.name()  # user
    repo_dict['files'] = []
    repo_dict['files'].append(file)
    return repo_dict


def file_add(dict,commit_dict):
    file_dict={}
    file_dict['path'] = dict['file_path']
    file_dict['commits'] = []
    file_dict['commits']=commit_dict
    return file_dict


def commit_add(com):
    commit_list = []

    i=0
    while i < len(com.date()):
        commit_dict = {}
        try:
            commit_dict['date'] = com.date()[i]
        except:
            commit_dict['date'] = None
        try:
            commit_dict['author'] = com.commit_author()[i] #chi fà commit
        except:
            commit_dict['author'] = None

        try:
            commit_dict['user'] = com.author()[i] #proprietario del file se presente
        except:
            commit_dict['user'] = None
        try:
            commit_dict['email'] = com.email()[i] #proprietario del file se presente
        except:
            commit_dict['email'] = None
        i+=1
        commit_list.append(commit_dict)
    return commit_list


def check_user(current_user, last_user, users_list, file):

    if (current_user == "__EOF_mde_2017__"):

        ''''
        developers_list = []
        emails_list = []
        for repository in users_list[0]['repositories']:
            for files in repository['files']:
                for commits in files['commits']:
                    developers_list.append(commits['author'])
                    emails_list.append(commits['email'])
        users_list[0]['developers_list'] = []
        users_list[0]['developers_list'] += set(developers_list)
        '''
        print(len(users_list))
        print("dump (finale) di", users_list[0])
        pickle.dump(users_list, file)
        file.close()
        return True

    if (current_user != last_user and last_user != None):
        return_dict = {}
        ''''
        developers_list = []

        for repository in users_list[0]['repositories']:
            for files in repository['files']:
                for commits in files['commits']:
                    developers_list.append(commits['author'])
        users_list[0]['developers_list'] = []
        users_list[0]['developers_list'] += set(developers_list)
        '''

        print(len(users_list))
        print("dump di", users_list[0])
        pickle.dump(users_list, file)
        return_dict['users_list'] = []
        return_dict['repos_list'] = []
        return return_dict


def repository_index(repos_list, name): #la usiamo per ritrovare, data la lista delle repositories di un utente, quella giusta in cui inserire il file nuovo
    index = 0
    for elem in repos_list:
        if(elem['name']==name):
            return index
        else:
            index += 1


def setup_list(main_file, final_file):

    objs = pickle.load(main_file)

    ###############
    objs_aux = []
    try:
        while True:
            try:
                o = pickle.load(final_file)
            except EOFError:
                break
            objs_aux.append(o)

        for elem in objs:
            if (elem['owner_name'] == objs_aux[-1]['name']):
                objs.remove(elem)
    ###############

        ########EOF trick########
        ordered_list_complete = objs[0]
        eof_dict = {}
        eof_dict['owner_name'] = "__EOF_mde_2017__"
        ordered_list_complete.append(eof_dict)
        #########################
    except:
        ordered_list_complete = objs

    return ordered_list_complete


def main_fun(g, tuple, file, param):

    users_list = param['users_list']
    repos_list = param['repos_list']
    duplicate = param['duplicate']
    current_last_user = param['current_last_user']

    current_user = tuple['owner_name']

    if(current_user=='__EOF_mde_2017__'):
        if(check_user(current_user, current_last_user, users_list, file)==True):
            return None

    new_dict = {}
    repo = g.get_repo(tuple['repository_name'])
    repos = repository(repo)
    utenti = users(g, repo)
    name = repos.repo_name()  # nome repository aggiunto per trovare l'indice dopo

    dict = check_user(current_user, current_last_user, users_list, file)#controllo dell'utente
    if(dict!=None):
        users_list = dict['users_list']
        repos_list = dict['repos_list']

    for elems in users_list:
        if(elems['user']==utenti.name()):#utente già presente

            for repositories in repos_list:#trovare un modo più furbo per fare sti controlli
                repository_duplicate = False
                if(repositories['name']==name):#repository già presente
                    com = commits(g, utenti.name() + "/" + repo.name, tuple['file_path'])

                    commit_dict = {}
                    commit_dict = commit_add(com)
                    elems['repositories'][repository_index(elems['repositories'], name)]['files'].append(file_add(tuple, commit_dict))#aggiunta del file

                    repository_duplicate = True
                    break

            if(repository_duplicate==False):
                #l'utente già c'è, ma la repository che sto guardando è nuova
                com = commits(g, utenti.name() + "/" + repo.name, tuple['file_path'])
                commit_dict = {}
                commit_dict = commit_add(com)
                repo_dict = repository_add(repos, utenti, file_add(tuple, commit_dict))#creazione dizionario repository
                elems['repositories'].append(repo_dict)
                repos_list.append(repo_dict)

            duplicate = True
            break

    if (duplicate == False):
        # se l'utente non c'è lo aggiungi e chiaramente anche la repository è nuova
        new_dict['user'] = utenti.name()
        new_dict['id'] = utenti.id()

        com = commits(g, utenti.name() + "/" + repo.name, tuple['file_path'])
        commit_dict = {}
        commit_dict = commit_add(com)

        repo_dict = repository_add(repos, utenti, file_add(tuple, commit_dict))#creazione dizionario repository
        new_dict['repositories'] = []
        new_dict['repositories'].append(repo_dict)

        users_list.append(new_dict) # registro finale
        repos_list.append(repo_dict)# registro la repository in una lista di supporto per tenere traccia degli utenti già controllati

    duplicate = False

    param['users_list'] = users_list
    param['repos_list'] = repos_list
    param['duplicate'] = duplicate
    param['current_last_user'] = current_user
    return param


def main():

    g = Github("", "")
    g1 = Github("", "")
    g2 = Github("", "")
    main_file = open('crawler_results/result_ordered.txt', 'rb')
    final_file = open('crawler_results/final_result.txt', 'wb')

    ordered_list_complete = setup_list(main_file, final_file)

###########setup##############
    users_list = []
    repos_list = []
    duplicate = False
    current_last_user = None
    starting_param = {}
    starting_param['users_list'] = users_list
    starting_param['repos_list'] = repos_list
    starting_param['duplicate'] = duplicate
    starting_param['current_last_user'] = current_last_user
###############################

    for tuple in ordered_list_complete:
        try:
            starting_param = main_fun(g, tuple, final_file, starting_param)
        except:
            print("eccezione su g")
            try:
                starting_param = main_fun(g1, tuple, final_file, starting_param)
            except:
                print("eccezione su g1")
                try:
                    starting_param = main_fun(g2, tuple, final_file, starting_param)
                except:
                    print("eccezione su g2")
                    continue

#main() #debug
