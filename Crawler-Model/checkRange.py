import pickle

def checkRange():

    try:
        file = open('crawler_results/result.txt', 'rb')
    except:
        return 0

    try:
        objs = []
        while True:
            try:
                o = pickle.load(file)
            except EOFError:
                break
            objs.append(o)
        result = 0
    except:
        file.close()
        return 1

    try:
        for inner_list in objs:
            for elem in inner_list:
                aux = elem['range']
                result = max(aux, result)
        return result
    except:
        file.close()
        return 1

#print('check_range_says:', checkRange()) just for debug
