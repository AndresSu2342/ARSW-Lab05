### Escuela Colombiana de Ingeniería

### Arquitecturas de Software



#### API REST para la gestión de planos.

En este ejercicio se va a construír el componente BlueprintsRESTAPI, el cual permita gestionar los planos arquitectónicos de una prestigiosa compañia de diseño. La idea de este API es ofrecer un medio estandarizado e 'independiente de la plataforma' para que las herramientas que se desarrollen a futuro para la compañía puedan gestionar los planos de forma centralizada.
El siguiente, es el diagrama de componentes que corresponde a las decisiones arquitectónicas planteadas al inicio del proyecto:

![](img/CompDiag.png)

Donde se definió que:

* El componente BlueprintsRESTAPI debe resolver los servicios de su interfaz a través de un componente de servicios, el cual -a su vez- estará asociado con un componente que provea el esquema de persistencia. Es decir, se quiere un bajo acoplamiento entre el API, la implementación de los servicios, y el esquema de persistencia usado por los mismos.

Del anterior diagrama de componentes (de alto nivel), se desprendió el siguiente diseño detallado, cuando se decidió que el API estará implementado usando el esquema de inyección de dependencias de Spring (el cual requiere aplicar el principio de Inversión de Dependencias), la extensión SpringMVC para definir los servicios REST, y SpringBoot para la configurar la aplicación:


![](img/ClassDiagram.png)

### Parte I

1. Integre al proyecto base suministrado los Beans desarrollados en el ejercicio anterior. Sólo copie las clases, NO los archivos de configuración. Rectifique que se tenga correctamente configurado el esquema de inyección de dependencias con las anotaciones @Service y @Autowired.
	
	Integramos el proyecto base anteriormente realizado en la parte I, tendriamos la siguiente estructura de nuestra aplicacion

	![Image](https://github.com/user-attachments/assets/20a024e1-8b58-4421-bc8d-99b2852c7775)

	Verificamos que esten las anotaciones @Service y @Autowired

    ![Image](https://github.com/user-attachments/assets/126c8210-34fe-4372-833f-4954a127f969)

2. Modifique el bean de persistecia 'InMemoryBlueprintPersistence' para que por defecto se inicialice con al menos otros tres planos, y con dos asociados a un mismo autor.

	Se agregaron tres nuevos planos al inicializar el bean de persistencia. Dos de ellos pertenecen al mismo autor para cumplir con la condición del ejercicio.
	
	![Image](https://github.com/user-attachments/assets/c41c3820-a1e9-4d3f-9c65-5cc1c65789ff)

3. Configure su aplicación para que ofrezca el recurso "/blueprints", de manera que cuando se le haga una petición GET, retorne -en formato jSON- el conjunto de todos los planos. Para esto:

	* Modifique la clase BlueprintAPIController teniendo en cuenta el siguiente ejemplo de controlador REST hecho con SpringMVC/SpringBoot:

	```java
	@RestController
	@RequestMapping(value = "/url-raiz-recurso")
	public class XXController {
    
        
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> manejadorGetRecursoXX(){
        try {
            //obtener datos que se enviarán a través del API
            return new ResponseEntity<>(data,HttpStatus.ACCEPTED);
        } catch (XXException ex) {
            Logger.getLogger(XXController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>("Error bla bla bla",HttpStatus.NOT_FOUND);
        }        
	}

	```
	* Haga que en esta misma clase se inyecte el bean de tipo BlueprintServices (al cual, a su vez, se le inyectarán sus dependencias de persisntecia y de filtrado de puntos).

	Basandonos en lo indicado por el ejercicio, finalmente tenemos que nuestra clase `BlueprintAPIController` queda de la siguiente manera:

   ![Image](https://github.com/user-attachments/assets/184661aa-e549-4ee0-83ce-b469edc1c820)

4. Verifique el funcionamiento de a aplicación lanzando la aplicación con maven:

	```bash
	$ mvn compile
	$ mvn spring-boot:run
	
	```
 
	Al usar estos comandos estabamos teniendo errores debido a las dependecias que manejabamos, ya que version de `Spring Boot` era incompantible con la version de `Java` que estabamos manejando. Finalmente nuestras dependencias quedaron de la siguiente manera:
	
	![Image](https://github.com/user-attachments/assets/e4d8ffac-18bc-47ab-ac00-b55071e3cf26)
	
	- Y luego enviando una petición GET a: http://localhost:8080/blueprints. Rectifique que, como respuesta, se obtenga un objeto jSON con una lista que contenga el detalle de los planos suministados por defecto, y que se haya aplicado el filtrado de puntos correspondiente.
	
	Efectivamente al consultar `http://localhost:8080/blueprints` obtenemos el siguiente jSON:
	
	![Image](https://github.com/user-attachments/assets/bd889dc1-14e4-4ac6-81cd-b9b0ee9b0fda)

5. Modifique el controlador para que ahora, acepte peticiones GET al recurso /blueprints/{author}, el cual retorne usando una representación jSON todos los planos realizados por el autor cuyo nombre sea {author}. Si no existe dicho autor, se debe responder con el código de error HTTP 404. Para esto, revise en [la documentación de Spring](http://docs.spring.io/spring/docs/current/spring-framework-reference/html/mvc.html), sección 22.3.2, el uso de @PathVariable. De nuevo, verifique que al hacer una petición GET -por ejemplo- a recurso http://localhost:8080/blueprints/juan, se obtenga en formato jSON el conjunto de planos asociados al autor 'juan' (ajuste esto a los nombres de autor usados en el punto 2).

	Agregamos el endpoint para obtener blueprints por autor:
	
	![Image](https://github.com/user-attachments/assets/1400a90d-0131-4b87-aad6-3fda1e2a4167)
	
	Al consultar un autor que existe, obtenemos la siguiente respuesta:
	
	![Image](https://github.com/user-attachments/assets/7d1b2ab8-aa6d-41f0-8acf-d590e1153502)
	
	Mientras que si consultamos un autor que no existe, obtenemos:
	
	![Image](https://github.com/user-attachments/assets/80c4f9e7-9f76-49d5-84dc-c89f7c0b9b76)

6. Modifique el controlador para que ahora, acepte peticiones GET al recurso /blueprints/{author}/{bpname}, el cual retorne usando una representación jSON sólo UN plano, en este caso el realizado por {author} y cuyo nombre sea {bpname}. De nuevo, si no existe dicho autor, se debe responder con el código de error HTTP 404. 

	Agregamos el endpoint para obtener el plano de un autor existente:
	
	![Image](https://github.com/user-attachments/assets/f019c176-0b18-49bf-ba91-5670b472d681)
	
	En caso de consultar un Autor y Plano que si existen, obtenemos la siguiente respuesta:
	
	![Image](https://github.com/user-attachments/assets/cb7c8922-b194-487d-9c09-3ac43727a22c)
	
	En caso de consultar un Autor que existe pero con un Plano que no tiene asociado, obtenemos:
	
	![Image](https://github.com/user-attachments/assets/b9453954-65f4-4aef-bd34-676fa6f2ab5b)

### Parte II

1.  Agregue el manejo de peticiones POST (creación de nuevos planos), de manera que un cliente http pueda registrar una nueva orden haciendo una petición POST al recurso ‘planos’, y enviando como contenido de la petición todo el detalle de dicho recurso a través de un documento jSON. Para esto, tenga en cuenta el siguiente ejemplo, que considera -por consistencia con el protocolo HTTP- el manejo de códigos de estados HTTP (en caso de éxito o error):

	```	java
	@RequestMapping(method = RequestMethod.POST)	
	public ResponseEntity<?> manejadorPostRecursoXX(@RequestBody TipoXX o){
        try {
            //registrar dato
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (XXException ex) {
            Logger.getLogger(XXController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>("Error bla bla bla",HttpStatus.FORBIDDEN);            
        }        
 	
	}
	```	

	Agregamos el método POST en el controlador para que nos permita recibir un Blueprint en formato jSON:
	
	![Image](https://github.com/user-attachments/assets/d01e16d3-680c-4560-a259-a5f253f31408)
	
	Para que funcione, tuvimos que modificar el método `addNewBlueprint` en la clase en que manejamos los servicios. Finalmente el método quedo de la siguiente manera:
	
	![Image](https://github.com/user-attachments/assets/621adba8-c364-40d7-9c0b-396048cfbb70)

2.  Para probar que el recurso ‘planos’ acepta e interpreta
    correctamente las peticiones POST, use el comando curl de Unix. Este
    comando tiene como parámetro el tipo de contenido manejado (en este
    caso jSON), y el ‘cuerpo del mensaje’ que irá con la petición, lo
    cual en este caso debe ser un documento jSON equivalente a la clase
    Cliente (donde en lugar de {ObjetoJSON}, se usará un objeto jSON correspondiente a una nueva orden:

	```	
	$ curl -i -X POST -HContent-Type:application/json -HAccept:application/json http://URL_del_recurso_ordenes -d '{ObjetoJSON}'
	```	

	Con lo anterior, registre un nuevo plano (para 'diseñar' un objeto jSON, puede usar [esta herramienta](http://www.jsoneditoronline.org/)):
	

	Nota: puede basarse en el formato jSON mostrado en el navegador al consultar una orden con el método GET.

	![Image](https://github.com/user-attachments/assets/a4f84ade-48b4-4141-94dc-6887b0eaf1b0)

	![Image](https://github.com/user-attachments/assets/b4e86059-780d-4dc0-8d23-c0917cfc423c)

	![Image](https://github.com/user-attachments/assets/1a60d711-9b87-4b9b-a1ba-f033d300b459)

3. Teniendo en cuenta el autor y numbre del plano registrado, verifique que el mismo se pueda obtener mediante una petición GET al recurso '/blueprints/{author}/{bpname}' correspondiente.

   ![Image](https://github.com/user-attachments/assets/867b8ef5-89a0-48b0-8db9-18d8958b3960)

4. Agregue soporte al verbo PUT para los recursos de la forma '/blueprints/{author}/{bpname}', de manera que sea posible actualizar un plano determinado.

	![Image](https://github.com/user-attachments/assets/494f69d8-ffd9-4bcd-94d9-78b1eeaac823)
	
	![Image](https://github.com/user-attachments/assets/59db3dd5-e265-4005-814f-6d1f144702a6)
	
	![Image](https://github.com/user-attachments/assets/e4f29fb2-b24e-44e6-b93a-7ac6f3c67859)
	
	![Image](https://github.com/user-attachments/assets/d7f491fc-27d5-4f32-bc59-085e6329ba26)
	
	![Image](https://github.com/user-attachments/assets/ccf9b54c-90e4-4dab-a3bd-5e72701acb4d)

### Parte III

El componente BlueprintsRESTAPI funcionará en un entorno concurrente. Es decir, atederá múltiples peticiones simultáneamente (con el stack de aplicaciones usado, dichas peticiones se atenderán por defecto a través múltiples de hilos). Dado lo anterior, debe hacer una revisión de su API (una vez funcione), e identificar:

* Qué condiciones de carrera se podrían presentar?
* Cuales son las respectivas regiones críticas?

Ajuste el código para suprimir las condiciones de carrera. Tengan en cuenta que simplemente sincronizar el acceso a las operaciones de persistencia/consulta DEGRADARÁ SIGNIFICATIVAMENTE el desempeño de API, por lo cual se deben buscar estrategias alternativas.

Escriba su análisis y la solución aplicada en el archivo ANALISIS_CONCURRENCIA.txt