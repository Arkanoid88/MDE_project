import search
import dictionaryManager
import modelCreator

def run():

    try:
        keyword = input("Insert the keyword (e.g. eclipse; main): ")
        extension = input("Insert the extension (e.g. mtl; ecore; java): ")
        search.main(keyword,extension)
        search.update()
        dictionaryManager.main()
        modelCreator.main_model_creator()
    except:
        search.update()
        search.update()
        dictionaryManager.main()
        modelCreator.main_model_creator()
        return 0

run()
