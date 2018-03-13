# Liferay REST Example

This example project shows how to create REST services using the JAX-RS technology.
This project also covers:
* How to wrap some exception thrown by your code to customize the REST service response.
* How to check if a User has permission to view any Journal Article (Liferay PermissionChecker)
* How to read the Journal Article’s content (SAXReaderUtil)
* How to read property files inside an OSGi module
* How to include external dependencies into the project updating manually the bnd.bnd file (Jackson)

### Prerequisites

* Install JDK
* Install Liferay Developer Studio
* Install, configure and start Liferay DXP

### Build and Deploy

* Clone this repo
* Import the module into Liferay Developer Studio
* Inside the Gradle tab, execute Build & Deploy
* Make sure to copy the generated .JAR ([projectdir]/build/libs) into your Liferay DXP deploy folder

## Endpoints

If the module is installed successfully, new values should appear within CXF Endpoints and Rest Extender settings.

* Control Panel > System > System Settings > Foundation > CXF Endpoints
* Control Panel > System > System Settings > Foundation > Rest Extender

This example uses the endpoint /user (if you are already using this endpoint, the application won't start)


## REST Usage

| URL | Method | Description |
| --- | --- | --- |
| http://localhost:8080/o/user/articles | GET | Returns all the Journal Article's content created with the Structure *Basic-web-content*. |

## Built With

* [Jackson](https://github.com/FasterXML/jackson) - The web framework used
* [Gradle](https://gradle.org/docs/) - Dependency Management
* [JAX-RS](https://docs.oracle.com/javaee/7/tutorial/jaxrs.htm) - Used to generate RSS Feeds 

## Authors

* **Angel Cazares** - *Initial work* - [AngelCazares](https://github.com/angelcazares)

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

## Acknowledgments

* [xtivia] https://github.com/xtivia/dxp-rest-example. Thanks!

