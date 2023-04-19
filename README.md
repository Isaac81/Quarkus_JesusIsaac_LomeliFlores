# Universidad de Guadalajara - Centro Universitario de Ciencias Exactas e Ingenierias
## Departamento de ciencias computacionales
Computación tolerante a fallas - Sección D06
Profesor: *López Franco Michel Emanuel*
Alumno: *Lomelí Flores Jesús Isaac*

## MicroProfile Quarkus

### Introducción
<p aling="justify">

</p>

### ¿Qué es Java EE?  


<p align="justify">
Java Platform, Enterprise Edition (Java EE) se basa en la especificación Java SE. Representa una colaboración entre varios proveedores y líderes del sector y proporciona el soporte de la infraestructura para las aplicaciones. Es portable y escalable, y soporta la integración existente y los componentes basados en la arquitectura EJB.

</p>

### ¿Qué es Java SE?  


<p align="justify">
Java Platform, Standard Edition (Java SE) es una especificación que describe una plataforma Java de resumen. Proporciona una base para crear y desplegar aplicaciones de negocio centradas en la red que van desde un ordenador de escritorio PC a un servidor de grupo de trabajo. Java SE lo implementa el kit de desarrollo de software (SDK) Java.
</p>

### ¿Qué es Jakarta EE? 


<p align="justify">
En 2017 Oracle decide que deja de controlar el desarrollo de Java EE y decide pasarlo a la comundidad para que sea guiado en un proceso más abierto y flexible por la Eclipse Foundation. Manteniendo el modelo de JCP que se seguía en su desarrollo.
</p>

<p align="justify">
Si bien en este proceso de traspaso se decide no pasar la marca Java EE y la comunidad tiene que renombrar el proyecto a Jakarta EE. En este proceso de renombrado, otros elementos como el servidor Glassfish también se han visto afectados y ahora se llama Eclipse Glassfish.
</p>

<p align="justify">
El nombre Jakarta EE es elegido por la comunidad Eclipse en una votación entre Jakarta EE y Enterprise Profile.
</p>

<p align="justify">
Así que Jakarta EE pasa a ser la nueva plataforma opensource de Java EE gestionada por Eclipse Foundation.
</p>

### ¿Qué es MicroProfile?  


<p align="justify">
MicroProfile es una especificación impulsada por la comunidad para proveer de una definición de plataforma de referencia que optimiza la arquitectura Java Enterprise de microservicios y ofrece portabilidad a las aplicaciones a través de múltiples tiempos de ejecución, incluyendo Open Liberty.
</p>

### ¿Qué es Spring boot?  


<p align="justify">
Java Spring Framework (Spring Framework) es un popular marco de trabajo empresarial de código abierto que sirve para crear aplicaciones autónomas de producción que se ejecutan en una máquina virtual Java (JVM).
</p>

<p align="justify">
Java Spring Boot (Spring Boot) es una herramienta que acelera y simplifica el desarrollo de microservicios y aplicaciones web con Spring Framework gracias a tres funciones principales:

1.  Configuración automática  
      
    
2.  Un enfoque de configuración obstinado  
      
    
3.  La capacidad de crear aplicaciones autónomas
</p>


### ¿Qué es Quarkus?  

<p align="justify"> 
Quarkus es un marco integral de Java diseñado para su implementación en Kubernetes y creado para las compilaciones propias y las máquinas virtuales Java (JVM). Permite optimizar Java especialmente para los contenedores y los entornos sin servidor, de nube y de Kubernetes.
</p>
<p align="justify"> 
Quarkus se diseñó para funcionar con las bibliotecas, los marcos y los estándares conocidos de Java, como Eclipse MicroProfile, Spring, Apache Kafka, RESTEasy (JAX-RS), Hibernate ORM (JPA), Infinispan, Camel y muchos más.
</p>

### ¿Qué es Maven?  

<p align="justify">
Apache Maven es una potente herramienta de gestión de proyectos que se utiliza para gestión de dependencias, como herramienta de compilación e incluso como herramienta de documentación. Es de código abierto y gratuita.
</p>

<p align="justify">
Aunque se puede utilizar con diversos lenguajes (C#, Ruby, Scala...) se usa principalmente en proyectos Java, donde es muy común ya que está escrita en este lenguaje. Frameworks Java como Spring y Spring Boot la utilizan por defecto.
</p>


### ¿Qué es Gradle?


<p align="justify">
Es una herramienta que permite la automatización de compilación de código abierto, la cual se encuentra centrada en la flexibilidad y el rendimiento. Los scripts de compilación de Gradle se escriben utilizando Groovy o Kotlin DSL (Domain Specific Language).
</p>

<p align="justify">
También cuenta con un sistema de gestión de dependencias muy estable. Gradle es altamente personalizable y rápido ya que completa las tareas de forma rápida y precisa reutilizando las salidas de las ejecuciones anteriores, sólo procesar las entradas que presentan cambios en paralelo.
</p>


### Desarrollo

<p align="justify">
Se desarrolla una API sencilla con una ruta llamada libros la cual se encarga de devolver los libros existentes en la lista de libros. Dicha ruta cuenta con una subruta llamada "first" encargada de devolver el primer libro de la lista. El código es el que se muestra debajo de este párrafo.
</p>


```java
package org.udgtl.controller;  
  
import org.eclipse.microprofile.faulttolerance.*;  
import org.udgtl.model.Libro;  
  
import javax.ws.rs.GET;  
import javax.ws.rs.Path;  
import java.util.ArrayList;  
import java.util.List;  
import java.util.Random;  
import java.util.logging.Logger;  
  
@Path("/libros")  
public class LibroController {  
  
	List<Libro> libroList = new ArrayList<>();  
	Logger LOGGER = Logger.getLogger("Demologer");  
	  
	  
	@GET  
	@Timeout(value = 5000L)  
	@Retry(maxRetries = 4)  
	@CircuitBreaker(failureRatio = 0.1, delay = 15000L)  
	@Bulkhead(value = 1)  
	@Fallback(fallbackMethod = "getLibroFallbackList")  
	public List<Libro> getLibroList(){  
		LOGGER.info("Devolviendo libros");  
		return this.libroList;  
	}  
	  
	@GET  
	@Path("/first")  
	@Timeout(value = 5000L)  
	@Retry(maxRetries = 4)  
	@CircuitBreaker(failureRatio = 0.1, delay = 15000L)  
	@Bulkhead(value = 1)  
	@Fallback(fallbackMethod = "getLibroFallbackList")  
	public List<Libro> getFirstLibro(){  
		LOGGER.info("obtieniendo la primer cancion");  
		return List.of(this.libroList.get(0));  
	}  
	  
	  
	public List<Libro> getLibroFallbackList(){  
		LOGGER.warning("Se produjo un error");  
		var libro = new Libro(-1l, "Default", "Autor por defecto");  
		return List.of(libro);  
	}  
}
```


Cuando se ejecuta la API se muestra la siguiente salida en consola.

![[Pasted image 20230419155029.png]]

Posteriormente se realiza una petición a la API para obtener todos los libros existentes en la ruta "/libros".

![[Pasted image 20230419155204.png]]

En la siguiente imagen se muestra la petición realizada en la terminal.

![[Pasted image 20230419155302.png]]

<p align="justify">
Para generar un fallo se realiza una petición a la ruta "/libros/first" que, al no contar la lista con un ningún libro, este método deberá fallar, razón por la cual se le ofrece un camino alternativo.
</p>


![[Pasted image 20230419155428.png]]


### Conclusión

<p align="justify">
Realizar esta actividad resulto de mucha ayuda para poder comprender como es que otros lenguajes de programación distintos a los que usualmente utilizo, implementan la tolerancia a fallos mediante implementación de código, como es el caso de una respuesta alternativa en caso de que el servicio no se encuentre disponible, a diferencia de algunas otras herramientas en otros lenguajes de programación que delegan estas funciones a la infraestructura existente.
</p>


## Bibliografía

- _Aplicaciones Java SE y Java EE_. (2021). © Copyright IBM Corp. 2013. Recuperado el 17 de Abril de 2023, de https://www.ibm.com/docs/es/odm/8.5.1?topic=application-java-se-java-ee-applications
- Cuervo, P. V. (2021, 2 marzo). _¿Qué es Jakarta EE?_ Arquitecto IT. Recuperado el 17 de Abril de 2023, de https://www.arquitectoit.com/java/que-es-jakarta-ee/
- _IBM Developer_. (2021, 6 julio). What is MicroProfile? Recuperado el 17 de Abril de 2023, de https://developer.ibm.com/series/what-is-microprofile/
- IBM Cloud. ¿Qué es Java Spring Boot? | IBM_. (s. f.). Recuperado el 17 de Abril de 2023, de https://www.ibm.com/es-es/topics/java-spring-boot
- Red Hat. ¿Qué es Quarkus? (2020, 13 enero). Recuperado el 17 de Abril de 2023, de https://www.redhat.com/es/topics/cloud-native-apps/what-is-quarkus
- Campus MVP. Java ¿Qué es Maven? ¿Qué es el archivo pom.xml? (2022, 1 junio). Recuperado el 17 de Abril de 2023, de https://www.campusmvp.es/recursos/post/java-que-es-maven-que-es-el-archivo-pom-xml.aspx
- Muradas, Y. (2023, 14 abril). _Qué es Gradle: La herramienta para ser más productivo desarrollando_. OpenWebinars.net. Recuperado el 17 de Abril de 2023, de https://openwebinars.net/blog/que-es-gradle/