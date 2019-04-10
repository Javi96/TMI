# -*- coding: utf-8 -*-


import urllib.request

'''
    Ejemplo de acceso a la API Rest implementada en Java
'''


'''
    Algunas de las funciones necesarias para parsear la llamada.

'''

'''
    Dada una entrada (supuestamente el nombre de un plato), parsea su contenido sustituyendo los espacios
    en blanco por "%20".
    
    Esto se utiliza cuando el plato está compuesto por múltiples palabras (por ejemplo, tortilla de patata).
    En el caso de que el nombre del plato esté compuesto por una sola palabra (por ejemplo, lentejas), este método
    no hará nada.
    
    Para realizar una petición get, es necesario convertir:
        tortilla de patatas ==>  tortilla%20de%20patatas
        
    el resultado es un string parseado tal y como se indica arriba.
    
'''
def parse_blank_spaces(input):
    #Si no tenemos una sola palabra, parsea. En caso contrario, no hace nada.
    return input.replace(" ", "%20") 

'''
    Dado un objeto bytes, obtiene el string asociado.
    
    Se utiliza para formatear el resultado de las llamadas a APIs que devuelven un json.
    
    input: objeto tipo bytes a formatear.
    
    el resultado es un objeto string
    
'''  
def parse_bytes_to_string(input):
    b"abcde".decode("utf-8") 
    return  input.decode('utf8') #Decodificamos usando utf-8. El resultado es un string.


'''
    EJEMPLO DE FUNCIONAMIENTO
'''

plate='tortilla de patatas'

plate=parse_blank_spaces(plate)

result = urllib.request.urlopen('http://localhost:5555/recipe/'+plate).read()



#Lo que devuelve la API es un objeto tipo bytes.
#Procesamos el objeto tipo bytes para obtener un string.
result = parse_bytes_to_string(result)

print(result)