
from pyswip import Functor, Variable, Query, call

assertz = Functor("assertz", 1)

# Los Functores de Prolog, primero los de la base de conocimientos
ingrediente = Functor("ingrediente", 1)
receta = Functor("receta", 2)
ingredienteReceta = Functor("ingredienteReceta", 2)
listaPasos = Functor("listaPasos", 2)
fotosReceta = Functor("fotosReceta", 2)

# Ahora los que me permiten manejar la búsquedas y demás
recetas = Functor("recetas", 2)

# Agregar elementos a la base de datos
call(assertz(ingrediente("Huevo")))
call(assertz(receta("Huevo Frito", "Desayuno")))
call(assertz(receta("Huevo Cocido", "Tentempié")))
call(assertz(ingredienteReceta("Huevo", "HuevoFrito")))
call(assertz(listaPasos('["Abrir el huevo", "Colocar Huevo en Sartén"]', "Huevo Frito")))

# Intento de imprimir las recetas ----------------------------------------------------------------------------

# Un sitio donde guardar los resultados
listaRecetas = []

# Las dos variables sin unificar de la búsqueda
X = Variable()
Y = Variable()

# Voy a hacer una consulta de todas las recetas
q = Query(receta(X, Y))

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
call(assertz("recetas(Receta, Ingrediente):- receta(Receta), ingrediente(Ingrediente), ingredienteReceta(Ingrediente, Receta)"))

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
