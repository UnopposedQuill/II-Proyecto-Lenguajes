
%Toda regla del Prolog del proyecto debe ser colocada aquí, todo lo que esté en este archivo se leerá *siempre* al crear un nuevo objeto objeto Prolog.

discontiguous ingrediente/1.
discontiguous receta/2.
discontiguous ingredienteReceta/2.
discontiguous listaPasos/2.

/*
Un wrapper de prolog para escribir nuevas cláusulas en la base de conocimientos
*/
escribirClausula(Clausula):-
	access_file('baseConocimientos.pl', append),
	open('baseConocimientos.pl', append, Stream),
	write_term(Stream, Clausula, [quoted=true, fullstop=true, nl=true]), close(Stream).

/*
Esta es la regla más importante, es la que permite hacer todas las búsquedas en la base de conocimientos.
*/
recetas(Receta, Ingrediente, TipoReceta, ListaPasos, ListaImagenes):-
	receta(Receta, TipoReceta), ingrediente(Ingrediente),
	ingredienteReceta(Ingrediente, Receta), listaPasos(ListaPasos, Receta),
	listaImagenes(ListaImagenes, Receta).
