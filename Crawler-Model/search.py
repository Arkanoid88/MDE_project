import os
import time
import pickle
import checkRange
from selenium import webdriver
from operator import itemgetter


def main(keyword, extension):

    driver = webdriver.Firefox(executable_path="geckodriver") # initiate a driver, in this case Firefox
    driver.get("https://www.github.com/login")

    if not os.path.exists('crawler_results'):
        os.makedirs('crawler_results')

    # log in
    username_field = driver.find_element_by_name('login') # get the username field
    password_field = driver.find_element_by_name('password') # get the password field
    #username_field.send_keys("") # enter in your username
    #password_field.send_keys("") # enter in your password
    username_field.send_keys("") # enter in your username
    password_field.send_keys("") # enter in your password
    password_field.submit() # submit it

    time.sleep(5)

    #cookie
    pickle.dump(driver.get_cookies(), open("QuoraCookies.pkl", "wb"))

    time.sleep(5)#per dare il tempo ai cookie

    #searching
    #idea: usare la size per raffinare la ricerca ed andare sotto i 1000 risultati

    lb = checkRange.checkRange() #lower bound iniziale per la dimensione calcolato sul file con i risultati che già abbiamo in modo da non dover ripartire sempre dall'inizio

    if(lb == 0): #upperd bound inziale
        ub = 500
    else:
        ub = lb*2

    while(True):
        #result file
        file = open('crawler_results/result.txt', 'ab')#  a per adding, w per write ogni volta che riapre
        page = 1
        lb = finder(lb, ub, page, driver, file, keyword, extension)
        if(lb==ub):
            ub = ub*2
            file.close()
        if(ub>100000):
            file.close()
            break

    update()


def update():
    file = open('crawler_results/result.txt', 'rb')  #file originale con la lista disordinata

    objs = []
    while True:
        try:
            o = pickle.load(file)
        except EOFError:
            break
        objs.append(o)


    file = open('crawler_results/result_ordered.txt', 'wb')  #nuovo file con la lista ordinata

    newlist = []
    for highelem in objs:
        for elem in highelem:
            print(elem)
            elem['owner_name'] = elem['repository_name'][0:elem['repository_name'].find('/')]
            newlist.append(elem)

    newlist = sorted(newlist, key=itemgetter('owner_name'))
    pickle.dump(newlist, file)


def finder(lb, ub, page, driver, file, keyword, extension):

    lista = []

    while(True):

        time.sleep(2)
        driver.get("https://github.com/search?p="+str(page)+"&q="+keyword+" extension%3A"+extension+" size%3A%22"+str(lb)+".."+str(ub)+"%22&type=Code&utf8=%E2%9C%93")
        for cookie in pickle.load(open("QuoraCookies.pkl", "rb")): #cookie
            new_cookie={}
            new_cookie['name'] = cookie['name']
            new_cookie['value'] = cookie['value']
            driver.add_cookie(new_cookie)

################################################################################################################################################
#inizio blocco ricerca del numero dei risultati
        try:
            #dovrebbe risolvere il problema dell'abuse
            for num_results in driver.find_elements_by_class_name('d-flex'):#cerca dentro la div d-flex, dove stanno i nomi delle repository
                try: #abuse managing
                    if(num_results.text.find('Showing')!=-1):#cerco nella pagina il numero di risultati
                        first_index = num_results.text.find('Showing')+8
                        last_index = num_results.text.find('available')-1
                        number_of_results = num_results.text[first_index:last_index]
                        aux = number_of_results.replace(",", "")
                        number_of_results = aux
                        break
                    if(num_results.text.find('code')!=-1):
                        last_index = num_results.text.find('code')-1
                        number_of_results = num_results.text[0:last_index]
                        aux = number_of_results.replace(",", "")
                        number_of_results = aux
                        break
                except:
                    time.sleep(10)
                    continue
        except:
            time.sleep(10)
            continue

        if(int(number_of_results)>1000):
            print(number_of_results)
            ub = int(ub - ((ub-lb)/2))
            print(ub)
            continue
#fine blocco
################################################################################################################################################

################################################################################################################################################
#inizio blocco ricerca nome repository, file

        results = driver.find_elements_by_class_name('d-inline-block')#nome della div class

        for elem in results:
            if(elem.text.find("."+extension)==-1):
                continue
            else:
                extension_index = elem.text.find('.'+extension)+len(extension)+1#pulizia stringa
                var_string = elem.text[0:extension_index]
                index = var_string.find(' ')
                pythonDictionary = {'repository_name':var_string[0:index],'range':ub,'file_name':elem.find_element_by_link_text(var_string[index+3:extension_index]).text ,'file_path':elem.find_element_by_link_text(var_string[index+3:extension_index]).get_attribute('title') }
                lista.append(pythonDictionary)

#fine blocco
################################################################################################################################################

        try:#quando ho raggiunto l'ultima pagina e non riesco a trovare la casellina con la pagina
            current_page = driver.find_element_by_class_name('current')
        except:
            return ub

        #if int(current_page.text) == 2:#debug
        #    break
        if int(current_page.text) == 30:
            time.sleep(30)
        if int(current_page.text) == 60:
            time.sleep(30)
        if int(current_page.text) == 90:
            time.sleep(30)
        if int(current_page.text) > 100:
            break

        page += 1

    pickle.dump(lista, file)
    file.close()
    return ub

#main("eclass", "ecore") #debug
#main("eclipse","mtl") #debug

#update() #debug
