# -*- coding: utf-8 -*-
"""
Created on Tue Apr 23 19:50:29 2019

@author: Bielo
"""

import spacy
import re
import random

nlp = spacy.load("en_core_web_sm")



string='1 cup og'

print(string[1:])

#doc = nlp(u"a lot of Yukoon Gold potatoes")
#doc = nlp(u"1 Some yukoon gold potatoes")
'''
print("a lot of Yukoon Gold potatoes")
print(parse("a lot of Yukoon Gold potatoes"))

print("small onion, quartered and thinly sliced")
print(parse("small onion, quartered and thinly sliced"))

print("6 large, very fresh eggs, preferably organic")
print(parse("6 large, very fresh eggs, preferably organic"))

print("2 tablespoons chicken stock or broth")
print(parse("2 tablespoons chicken stock or broth"))
'''

#doc = nlp(u"a lot of Yukoon Gold potatoes")
#doc = nlp(u"1 Some yukoon gold potatoes")

'''
for ent in doc.ents:
    print(ent.text, ent.start_char, ent.end_char, ent.label_)
'''
#for chunk in doc.noun_chunks:
#    print(chunk.text)
'''
span = doc[doc[4].left_edge.i : doc[4].right_edge.i+1]
with doc.retokenize() as retokenizer:
    retokenizer.merge(span)
'''
'''
for token in doc:
    print(token.text, token.pos_)

for chunk in doc.noun_chunks:
    print(chunk.text, chunk.root.text, chunk.root.dep_,
            chunk.root.head.text)
'''

#span = doc[doc[4].left_edge.i : doc[4].right_edge.i+1]
#with doc.retokenize() as retokenizer:
#    retokenizer.merge(span)
'''
for token in doc:
    print(token.text, token.pos_)
'''

#Obtener los cardinales.
#for ent in doc.ents:
#    print(ent.text)
'''
doc = nlp(u"small onion, quartered and thinly sliced")
for chunk in doc.noun_chunks:
    print(chunk.text)
for token in doc:
    print(token.text, token.pos_)

doc = nlp(u"6 large, very fresh eggs, preferably organic")
for chunk in doc.noun_chunks:
    print(chunk.text)

doc = nlp(u"2 tablespoons chicken stock or broth")



for chunk in doc.noun_chunks:
    print(chunk.text)
'''