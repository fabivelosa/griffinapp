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
	stage ('Initialize') {
            steps {
                sh '''
                    echo "PATH = ${PATH}"
                    echo "M2_HOME = ${M2_HOME}"
                '''
            }
        }
        stage('Build') { 
            steps {
                echo "This is the build stage." 
		        echo "${WORKSPACE}"
                 sh "ls -la ${WORKSPACE}"
                 sh "rm -rf prodigiesApp"
		sh "mvn clean -f prodigiesApp"
		sh "mvn install -f prodigiesApp"
            }
        }
        stage('Test') { 
            steps {
                echo "This is the test stage." 
		 sh "mvn test -f prodigiesApp"
            }
        }
        stage('Git Forensics'){
            steps{
               echo "Mines Repo"
            }
        }
    }
    
    post {  
         always {  
            echo 'This will always run'  
            mineRepository()
         }  
         success {  
             echo 'This will run only if successful'  
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
