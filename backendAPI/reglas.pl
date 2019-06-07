
%Toda regla del Prolog del proyecto debe ser colocada aquí, todo lo que esté en este archivo se leerá *siempre* al crear un nuevo objeto objeto Prolog.

discontiguous ingrediente/1.
discontiguous receta/2.
discontiguous ingredienteReceta/2.
discontiguous listaPasos/2.
discontiguous listaImagenes/2.
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
recetas(Receta, Ingrediente, TipoReceta):- 
	receta(Receta, TipoReceta), ingrediente(Ingrediente),
	ingredienteReceta(Ingrediente, Receta).

/*Esta regla retorna toda la informacion detallada de una receta*/
append3([], List, List).
append3([Head|Tail], List, [Head|Rest]) :-
    append3(Tail, List, Rest).
flatten2([],[]).
flatten2([H|T],P):-!,flatten2(T,N),append3(H,N,P).
pasos([],[]).
pasos(P,L):-flatten2(L,P).
infoReceta(R,I,T,P,L):-
  receta(R,T),
  findall(X,ingredienteReceta(X,R),I),
  findall(Y,listaPasos(Y,R),N),pasos(P,N),
  findall(Z,listaImagenes(Z,R),L).
