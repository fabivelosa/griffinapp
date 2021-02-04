pipeline {
    agent any 
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
}
