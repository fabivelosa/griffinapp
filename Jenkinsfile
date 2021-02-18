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
	         sh "mvn clean -f prodigiesApp"
             sh "mvn pmd:pmd -f prodigiesApp"
             sh "mvn checkstyle:checkstyle -f prodigiesApp"
            }
        }    
        stage('Unit Test') { 
            steps {
		sh "mvn test -f prodigiesApp"
            }
        }
        stage('Integration Test') { 
            steps {
		       echo "This is the integration test stage"
		       sh "mvn test -Parquillian-test -f prodigiesApp"
            }
        }
        stage('Build') { 
            steps {
		sh "mvn install -f prodigiesApp"
            }
        }
        stage('Findbugs Analysis'){
            steps{
            sh "mvn findbugs:findbugs -f prodigiesApp"
            }
        }

    }
    
    post {  
         always {  
            echo 'This action mines the repository for commit forensics'  
            mineRepository()
	    junit 'prodigiesApp/target/surefire-reports/*.xml'
	    recordIssues enabledForFailure: true, sourceCodeEncoding: 'UTF-8', tools: [pmdParser()]
        recordIssues enabledForFailure: true, tools: [checkStyle()] 
	    jacoco exclusionPattern: '**/*Test*.class', inclusionPattern: '**/*.class', runAlways: true    
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
