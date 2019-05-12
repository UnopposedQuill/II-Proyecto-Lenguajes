
from pyswip import Prolog, Functor, Variable, Query, call

prolog = Prolog()

# Agregar elementos a la base de datos
prolog.assertz(ingrediente("Huevo"))
prolog.assertz(receta("Huevo Frito", "Desayuno"))
prolog.assertz(receta("Huevo Cocido", "Tentempié"))
prolog.assertz(ingredienteReceta("Huevo", "HuevoFrito"))
prolog.assertz(listaPasos('["Abrir el huevo", "Colocar Huevo en Sartén"]', "Huevo Frito"))

# Intento de imprimir las recetas ----------------------------------------------------------------------------

# Un sitio donde guardar los resultados
listaRecetas = []

# Voy a hacer una consulta de todas las recetas
q = Prolog.query(receta(X, Y))

# Por cada solución
while q.nextSolution():
    # Agregar a los resultados
    listaRecetas.append([X.value, Y.value])

# Cierro la consulta
q.closeQuery()

# Imprimir Resultados
for i in listaRecetas:
    print(i[0], i[1])

# Ahora sólo las que tengan huevo como ingrediente ------------------------------------------------------------

# Agrego una regla que me retorna todas las recetas de la lista de conocimientos
prolog.assertz("recetas(Receta, Ingrediente):- receta(Receta), ingrediente(Ingrediente), ingredienteReceta(Ingrediente, Receta)"))

# Un sitio donde guardar los resultados
listaRecetas = []

# Las dos variables sin unificar de la búsqueda
X = Variable()
Y = Variable()

# Voy a hacer una consulta de todas las recetas
q = Query(recetas(X, Y))

# Por cada solución
while q.nextSolution():
    # Agregar a los resultados
    listaRecetas.append([X.value])

# Cierro la consulta
q.closeQuery()

# Imprimir Resultados
for i in listaRecetas:
    print(i[0])

# Outputs:
#
