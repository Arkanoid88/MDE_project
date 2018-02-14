from pyecore.resources import ResourceSet, URI
from pyecore.utils import DynamicEPackage
import pickle


def check_user(instance, user):
    for x in instance.users:

        if(user!=None):
            if (user == x.name):
                return x  # deve tornare un riferimento ad user
        else:
            return None


def check_developer(instance, developer):
    for x in instance.developers:

        if(developer!=None):
            if (developer == x.name):
                return x  # deve tornare un riferimento ad user
        else:
            return None


def insert_from_dict(dict, MyMetamodel, a_instance, tech_instance):

    for elem in dict:
        for user in elem:  # aggiunta utente
            first_instance = MyMetamodel.User()
            first_instance.name = user['user']
            first_instance.id = str(user['id'])
            for repository in user['repositories']:  # aggiunta repository
                second_instance = MyMetamodel.Repository()
                second_instance.name = repository['name']
                second_instance.stargazers = repository['stargazers']
                second_instance.watchers = repository['watchers']
                second_instance.forks = repository['forks']
                second_instance.fork = repository['fork']
                second_instance.fork = repository['fork']
                second_instance.size = repository['size']
                second_instance.owner = first_instance  # aggiungo lista repositories
                for file in repository['files']:  # aggiunta file
                    third_instance = MyMetamodel.File()
                    third_instance.path = file['path']
                    third_instance.repository = second_instance
                    third_instance.technologies.append(tech_instance)
                    for commit in file['commits']:
                        fourth_instance = MyMetamodel.Commit()
                        # print(elem2)
                        # print(elem4['date'].date())
                        fourth_instance.date = str(commit['date'].date())
                        fourth_instance.user = check_user(a_instance, commit['user'])  # ereference a user
                        fourth_instance.author = check_developer(a_instance,
                                                                 commit['author'])  # ereference a developers
                        third_instance.commits.append(fourth_instance)
                    second_instance.files.append(third_instance)  # aggiungo lista dei file
                first_instance.repositories.append(second_instance)
            a_instance.users.append(first_instance)

    return a_instance


def create_developers(dicta, a_instance, MyMetamodel):

    developers_list = []
    for elem in dicta:
        for repository in elem[0]['repositories']:
            for files in repository['files']:
                for commits in files['commits']:
                    # developers_list.append(commits['author'])
                    aux_dict = {}
                    aux_dict['name'] = commits['author']
                    aux_dict['email'] = commits['email']
                    developers_list.append(aux_dict)

    developers_list = [dict(t) for t in set([tuple(d.items()) for d in developers_list])]
    print(developers_list)

    for elem in developers_list:
        first_instance = MyMetamodel.Developer()
        first_instance.name = elem['name']
        first_instance.email = elem['email']
        a_instance.developers.append(first_instance)

    return a_instance


def create_technology(main_list, a_instance, MyMetamodel):

    for elem in main_list:
        string = elem[0]['repositories'][0]['files'][0]['path']
        index = string.rfind(".")
        extension_string = string[index + 1:]
    tech_instance = MyMetamodel.Technology()
    tech_instance.name = extension_string
    tech_instance.title = extension_string.upper()
    a_instance.technologies.append(tech_instance)

    return tech_instance


def setup_list():
    file = open('crawler_results/final_result.txt', 'rb')
    objs = []
    while True:
        try:
            o = pickle.load(file)
        except EOFError:
            break
        objs.append(o)

    for elem in objs:
        for subelem in elem:
            print(subelem)
    return objs


def model_setup():

    rset = ResourceSet()
    resource = rset.get_resource(URI('metamodel.ecore'))
    mm_root = resource.contents[0]

    mm_root.nsURI = 'ghmde'
    mm_root.nsPrefix = 'model'
    mm_root.name = 'ghmde'

    rset.metamodel_registry[mm_root.nsURI] = mm_root

    print(mm_root.nsURI)

    MyMetamodel = DynamicEPackage(mm_root)  # We create a DynamicEPackage for the loaded root

    file_instance = MyMetamodel.File()

    print(file_instance)

    A = mm_root.getEClassifier('Model')

    # nuovo
    instance = A()

    dictionary = {}
    dictionary['model'] = instance
    dictionary['meta-model'] = MyMetamodel

    return dictionary


def main_model_creator():

    rset = ResourceSet()
    main_list = setup_list()
    setup = model_setup()
    a_instance = setup['model']
    MyMetamodel = setup['meta-model']

    try: #modello già presente

        resource = rset.get_resource(URI('metamodel.ecore'))
        mm_root = resource.contents[0]
        rset.metamodel_registry[mm_root.nsURI] = mm_root
        resource = rset.get_resource(URI('model.xmi'))
        model_root = resource.contents[0]

        for user in model_root.users:  # aggiunta utente
            print(user.name)
            for repository in user.repositories:  # aggiunta repository
                print(repository.name)



        ######### inserimento di developers nel modello ##########
        a_instance = create_developers(main_list, a_instance, MyMetamodel)
        ##########################################################

        ######### inserimento delle technologies nel modello ##########
        tech_instance = create_technology(main_list, a_instance, MyMetamodel)
        ###############################################################

        ########## filling model ############
        a_instance = insert_from_dict(main_list, MyMetamodel, a_instance, tech_instance)
        #####################################

##########################################
        first_instance = MyMetamodel.User()
        first_instance.name = model_root.users[0].name
        a_instance.users.append(first_instance)
##########################################

        save_resource = rset.create_resource('new_model.xmi')
        save_resource.append(a_instance)
        save_resource.save()
        return 0

    except: #modello nuovo

        ######### inserimento di developers nel modello ##########
        a_instance = create_developers(main_list, a_instance, MyMetamodel)
        ##########################################################

        ######### inserimento delle technologies nel modello ##########
        tech_instance = create_technology(main_list, a_instance, MyMetamodel)
        ###############################################################

        ########## filling model ############
        a_instance = insert_from_dict(main_list, MyMetamodel, a_instance, tech_instance)
        #####################################

        save_resource = rset.create_resource('model.xmi')
        save_resource.append(a_instance)
        save_resource.save()


def load():

    #return model_root
    rset = ResourceSet()

    resource = rset.get_resource(URI('metamodel.ecore'))
    mm_root = resource.contents[0]
    rset.metamodel_registry[mm_root.nsURI] = mm_root

    resource = rset.get_resource(URI('model.xmi'))
    model_root = resource.contents[0]
    MyMetamodel = DynamicEPackage(mm_root)


#main_model_creator() #debug