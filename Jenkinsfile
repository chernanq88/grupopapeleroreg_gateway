pipeline{
	
	agent any
	
	environment{
		ARTIFACT_ID = readMavenPom().getArtifactId()
	}
	
	stages{ 
	
		stage('Maven Build') {
		    environment {
                        JAVA_HOME = "/usr/lib/jvm/java-1.11.0-openjdk-amd64"
        	    }
		    steps{
			sh "mvn -s $SETTINGS clean install"
		    }  
		}
	
		stage('Deploy on grupopapeleroreg.com') {
	      	        steps{
	   	                sh '''#!/bin/bash \n
				     ssh -o StrictHostKeyChecking=no root@104.192.5.97 -i /var/lib/jenkins/.ssh/id_rsa_psigo /home/microservicios/gateway-stop.sh &   
				   '''
				sh "scp -i /var/lib/jenkins/.ssh/id_rsa_psigo $WORKSPACE/target/${ARTIFACT_ID}.jar root@104.192.5.97:/home/microservicios/${ARTIFACT_ID}.jar"
				sh '''#!/bin/bash \n
				     ssh -o StrictHostKeyChecking=no root@104.192.5.97 -i /var/lib/jenkins/.ssh/id_rsa_psigo /root/gateway.sh &   
				   '''
	    	        }
	        }
	}
}
