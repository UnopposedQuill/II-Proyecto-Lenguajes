
from pyswip import Prolog

prolog = Prolog()

print(list(prolog.query('ls')))
prolog.consult('reglas.pl')
prolog.consult('baseConocimientos.pl')

# prolog.assertz('ingrediente("Arroz")')

print(list(prolog.query('escribirClausula("ingrediente(Arroz).")')))

prolog.consult('baseConocimientos.pl')
