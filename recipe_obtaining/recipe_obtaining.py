# -*- coding: utf-8 -*-


'''
    IMPORTS NECESARIOS
'''

from bottle import route, run, template
from recipe_module import get_recipe

'''
    FUNCIONALIDADES PRINCIPALES DEL SERVICIO REST
'''

'''
    ARRANQUE DEL SERVICIO DEL API REST
'''
@route('/recipe/<plate>')
def index(plate):
    #return get_recipe(plate)
    return str(get_recipe(plate))

    
run(host='localhost', port=8080)
