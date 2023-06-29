pipeline {
    agent any

    stages {

        stage('Checkout') {
            steps {
                checkout([
                    $class: 'GitSCM',
                    branches: [[name: 'develop']],
                    userRemoteConfigs: [[
                        url: 'http://192.168.1.69/dev-teca-qlnv/qlnv-hang.git',
                        credentialsId: 'gitlab-connect'
                    ]],
                    changelog: true
                ])
            }
        }

        stage('Build') {
            steps {
                script {
                    withMaven(maven: 'maven') {
                        sh 'pwd'
                        sh "cd /home/jenkins/workspace/QLNV-HANG-62/src/main/resources && sed -i 's/192\\.168\\.1\\.80/192.168.1.62/g' ./bootstrap.yml"
                        // Chạy lệnh Maven build
                        sh 'mvn clean package -Dmaven.test.skip'
                    }
                }
            }
        }

        stage('Deploy') {
            steps {
                script {
                    sh 'scp -i /home/jenkins/.ssh/id_rsa /home/jenkins/workspace/QLNV-HANG-62/target/qlnv-hang-0.0.1.jar.original root@192.168.1.62:/u01/QLNV'
                    sh 'ssh -i /home/jenkins/.ssh/id_rsa root@192.168.1.62 "systemctl restart qlnv-hang.service"'
                }
            }
        }

    }
// 	post {
//         success {
//           sh 'curl -s -X POST https://api.telegram.org/bot6143753869:AAGyCTLkvhSDDDHRV64nS_q-ylAP7lFjRj8/sendMessage -d chat_id=-753301249 -d text="Đã build bản code mới thành công"'
//         }

//         failure {
//             sh 'curl -s -X POST https://api.telegram.org/bot6143753869:AAGyCTLkvhSDDDHRV64nS_q-ylAP7lFjRj8/sendMessage -d chat_id=-753301249 -d text="Build bản code mới thất bại"'
//         }
//     }
}