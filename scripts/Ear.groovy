includeTargets << grailsScript("_GrailsWar")

target(ear: "Creates an EAR file from a Grails WAR") {
	war()	
	event("StatusUpdate", ["Building EAR file"])
	generateApplicationXml()
        
        def metaInfDir = new File("${basedir}/src/META-INF")
	def warDest = new File(warName).parentFile
	def earFile = "${projectTargetDir}/${contextRoot}.ear"
	ant.ear(destfile:earFile, appxml:appXml, update:true) {
	        if(metaInfDir.exists()){
	            metainf(dir:metaInfDir)
	        }
		fileset(dir:warDest, includes:"*.war")
	}
	event("StatusFinal", ["Done creating EAR $earFile"])
}

target(defineContextRoot:"defines the context root") {
	contextRoot = "${grailsAppName}${grailsAppVersion ? '-'+grailsAppVersion : ''}"	
}

target(generateApplicationXml:"Generates an application.xml file") {
	depends(defineContextRoot)
	def warDest = new File(warName)
	appXml = "${projectTargetDir}/application.xml"
	new File(appXml).write """\
<?xml version="1.0" encoding="UTF-8"?> 
<application xmlns="http://java.sun.com/xml/ns/j2ee" xmlns:xsi="http://www.w3.org/ 2001/XMLSchema-instance" 
       xsi:schemaLocation="http:// java.sun.com/xml/ns/j2ee 
           http://java.sun.com/xml/ns/j2ee/ application_1_4.xsd" 
       version="1.4"> 
<display-name>${grailsAppName}</display-name> 

<module> 
    <web> 
        <web-uri>${warDest.name}</web-uri> 
        <context-root>${contextRoot}</context-root> 
    </web> 
 </module> 

</application>	
"""
}

setDefaultTarget(ear)
