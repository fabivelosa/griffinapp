pipeline {
    agent any 
    
    environment {
        EMAIL_TO = 'A00278899@student.ait.ie'
    }

    tools { 
        maven 'Maven 3.3.6' 
        jdk 'jdk8' 
    }
    
    stages {
	stage ('Static Code Analysis') {
            steps {
                sh "mvn pmd:pmd -f prodigiesApp"
            }
        }
        stage('Build') { 
            steps {
		sh "mvn clean -f prodigiesApp"
		sh "mvn install -f prodigiesApp"
            }
        }
        stage('Test') { 
            steps {
		 sh "mvn test -f prodigiesApp"
            }
        }
        stage('Generate Test Reports') { 
            steps {
	    junit 'prodigiesApp/target/surefire-reports/*.xml'
	    jacoco exclusionPattern: '**/*Test*.class', inclusionPattern: '**/*.class', runAlways: true       
	}
        }
    }
    
    post {  
         always {  
            echo 'This action mines the repository for commit forensics'  
            mineRepository()
         }  
         success {  
            echo 'This action archives the jar files in the successful build'  
	    archiveArtifacts artifacts: 'prodigiesApp/target/*.jar', followSymlinks: false         
	 }  
         failure {  
             echo 'This will run only on failure'  
         }  
         unstable {  
             echo 'This will run only if the run was marked as unstable'  
         }  
         changed {  
             echo 'This will run only if the state of the Pipeline has changed'  
             echo 'For example, if the Pipeline was previously failing but is now successful'  
         }  
     }      
}
