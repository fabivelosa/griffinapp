pipeline {
    agent any 
    
    environment {
        EMAIL_TO = 'A00278899@student.ait.ie'
    }
    
    
    stages {
        stage('Build') { 
            steps {
                echo "This is the build stage." 
		        echo "${WORKSPACE}"
                sh "ls -la ${WORKSPACE}"
                // sh "rm -rf my-app"
		// sh "mvn clean -f my-app"
		// sh "mvn install -f my-app"
            }
        }
        stage('Test') { 
            steps {
                echo "This is the test stage." 
		// sh "mvn test -f my-app"
            }
        }
    }
    
    post {  
         always {  
             echo 'This will always run'  
            
             committerEmail = sh (
                  script: 'git --no-pager show -s --format=\'%ae\'',
                  returnStdout: true
             ).trim()
             
             mail bcc: "", body: "<b>Example</b><br>Project: ${env.JOB_NAME} <br>Build Number: ${env.BUILD_NUMBER} <br> URL de build: ${env.BUILD_URL}", cc: '', charset: 'UTF-8', from: '', mimeType: 'text/html', replyTo: '', subject: "ERROR CI: Project name -> ${env.JOB_NAME}", to: "${committerEmail}";
         }  
         success {  
             echo 'This will run only if successful'  
         }  
         failure {  
               mail bcc: '', body: "<b>Example</b><br>Project: ${env.JOB_NAME} <br>Build Number: ${env.BUILD_NUMBER} <br> URL de build: ${env.BUILD_URL}", cc: '', charset: 'UTF-8', from: '', mimeType: 'text/html', replyTo: '', subject: "ERROR CI: Project name -> ${env.JOB_NAME}", to: "A00278899@student.ait.ie";  
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
