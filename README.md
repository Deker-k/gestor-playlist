# Gestor de lista de reproducción (lista doblemente enlazada circular)

Proyecto en Java que administra una lista de canciones con una **lista doblemente enlazada circular** implementada desde cero (sin `LinkedList`, `ArrayList`, arreglos ni otras colecciones).

## Integrantes

* Acosta Avila Diego Ernesto
* Candelaria Rosas Victor Eduardo
* Lopez Contreras Derek Kareem
* Mario Alberto Martinez Ovalle



## Versión de Java

Java 11 o superior (probado con el JDK instalado en el entorno de desarrollo). Solo se usa la biblioteca estándar.

## Estructura del proyecto

```
src/
  Cancion.java            Modelo de datos (id, título, artista, duración > 0)
  Nodo.java               Nodo con referencias anterior y siguiente
  ListaReproduccion.java  Lista circular: inicio, fin, actual y contador
  Menu.java               Interfaz de consola (13 opciones)
  Pruebas.java            Pruebas reproducibles (sin librerías externas)
```

## Compilación

Desde la raíz del proyecto:

En Windows (PowerShell o terminal de IntelliJ), desde la carpeta `gestor-playlist`:

```powershell
mkdir out
javac -encoding UTF-8 -d out src\\\*.java
```

## Ejecución del programa

```powershell
java -cp out Menu
```

> \*\*Nota (acentos y símbolos):\*\* si la consola muestra `?` en lugar de acentos o `▶`, ejecuta con
> `java -Dstdout.encoding=UTF-8 -cp out Menu` (en Windows, además, `chcp 65001` antes de ejecutar).

## Ejecución de las pruebas

```powershell
java -cp out Pruebas
```

Cada prueba identifica el escenario y muestra si el resultado coincide con lo esperado, además del tamaño final y la consistencia de los enlaces. El programa termina con código de salida `0` si todas pasan.

## Decisiones de diseño

* Los identificadores se generan con un contador incremental (no se reutilizan), por lo que insertar es O(1) sin buscar duplicados.
* Las búsquedas usan el contador de elementos como condición de término (nunca `null`), evitando ciclos infinitos.
* `ListaReproduccion` no imprime: devuelve valores (`null`, `false`, objetos) y `Menu` decide cómo mostrarlos.
* Al eliminar un nodo se conservan `anterior` y `siguiente` antes de reenlazar, se actualizan `inicio`, `fin` y `actual`, y se anulan los enlaces del nodo eliminado.
* `esConsistente()` verifica todos los invariantes de la estructura y se usa en las pruebas.

## Operaciones del menú

1. Agregar canción al inicio
2. Agregar canción al final
3. Buscar canción por ID
4. Seleccionar canción por ID (hacerla actual)
5. Eliminar canción por ID
6. Eliminar la canción actual
7. Consultar canción actual
8. Avanzar a la siguiente canción
9. Retroceder a la canción anterior
10. Mostrar lista en ambos sentidos
11. Consultar cantidad de canciones
12. Simular reproducción de k canciones
13. Salir

## Complejidades (n = cantidad de canciones)

|Operación|Complejidad|
|-|-|
|Agregar al inicio o al final|O(1)|
|Buscar / seleccionar por id|O(n)|
|Eliminar por id|O(n) (búsqueda) + O(1) (reenlace) = O(n)|
|Eliminar la canción actual|O(1)|
|Consultar canción actual, avanzar, retroceder, contar|O(1)|
|Mostrar todas las canciones|O(n)|
|Simular k reproducciones|O(k)|



