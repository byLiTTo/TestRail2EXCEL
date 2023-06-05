# QapterClaims FR Maintenance Tool

**English:**    
This repository has been created for the QA team of Solera inc in the QapterClaims France project.

Every night they run a test plan of automated testcases and generate results, the functionality of this program is to collect all the cases that have failed or have been postponed and create an excel to facilitate the organization of the team and have a history of the results.

**Español:**   
Este repositorio ha sido creado para el equipo de QA de Solera inc en el proyecto QapterClaims Francia.

Todas las noches se ejecuta un test plan de testcases automatizados y generan unos resultados, la funcionalidad de este programa es recopilar todos los casos que han fallado o que han sido postpuestos y crear un excel para facilitar la organización del equipo y tener un historial de los resultados.

# Introducing the interface
## Main window

<p align="center">
  <img src="https://github.com/byLiTTo/TestRail2EXCEL/blob/master/img/Menu.png" height="350" />
</p>

**English:**   
Next, we will explain the interface and its different functionalities.
1. Browser: Choose the browser application that will be used to launch the program, by default Google Chrome. (For now, the functionality has been implemented in the default browser).
2. CTFR Name: Name of the test plan of the pre-production environment where the results will be searched, by default the name is *QAPTER_CLAIMS_FR_AUTO_REGRESSION_CTFR_06-2023*.
3. INT1FR Name: Name of the test plan of the development environment where the results will be searched, by default the name is *QAPTER_CLAIMS_FR_AUTO_REGRESSION_INT1FR_06-2023*.
4. CTFR File: Path of the excel file where the information obtained from the test plan of point 2 will be loaded. It will follow the format *./CTFR_Maintenance_MONTH_YEAR.xlsx*.
5. INT1FR File: Path of the excel file where the information, obtained from the test plan of point 3, will be loaded. It will follow the format *./INT1FR_Maintenance_MONTH_YEAR.xlsx*.
6. Open (CTFR): Button that opens a pop up that opens the file explorer to find the location of the excel file to be used for CTFR.
7. Open (INT1FR): Button that opens a pop up that opens the file explorer to search for the location of the excel file to be used for INT1FR.
8. Update: Button that launches the program.
9. Running version of the program.

**Español:**   
A continuación, vamos a explicar la interfaz y sus diferentes funcionalidades.
1. Browser: Escoge la aplicación de navegador que se utilizará para lanzar el programa, por defecto Google Chrome. (Por ahora solo se ha implementado la funcionalidad en el navegador por defecto).
2. CTFR Name: Nombre del test plan del entorno de preproducción donde se buscarán los resultados, por defecto el nombre es *QAPTER_CLAIMS_FR_AUTO_REGRESSION_CTFR_06-2023*
3. INT1FR Name: Nombre del test plan del entorno de desarrollo donde se buscarán los resultados, por defecto el nombre es *QAPTER_CLAIMS_FR_AUTO_REGRESSION_INT1FR_06-2023*
4. CTFR File: Ruta del fichero excel donde se cargará la información, obtenida del test plan del punto 2. Seguirá el formato *./CTFR_Maintenance_MONTH_YEAR.xlsx*
5. INT1FR File: Ruta del fichero excel donde se cargará la información, obtenida del test plan del punto 3. Seguirá el formato *./INT1FR_Maintenance_MONTH_YEAR.xlsx*
6. Open (CTFR): Botón que abre un pop up que abre el explorador de archivos para buscar la localización del fichero excel que se usará para CTFR.
7. Open (INT1FR): Botón que abre un pop up que abre el explorador de archivos para buscar la localización del fichero excel que se usará para INT1FR.
8. Update: Botón que lanza el programa.
9. Versión que se está ejecutando del programa.