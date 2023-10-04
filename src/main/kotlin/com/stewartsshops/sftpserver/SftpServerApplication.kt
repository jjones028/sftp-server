package com.stewartsshops.sftpserver

import org.apache.sshd.scp.server.ScpCommandFactory
import org.apache.sshd.server.SshServer
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider
import org.apache.sshd.server.shell.InteractiveProcessShellFactory
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Component
import java.io.File

@SpringBootApplication
class SftpServerApplication

fun main(args: Array<String>) {
    runApplication<SftpServerApplication>(*args)
}

@Component
class SftpServer() : ApplicationRunner {

    @Throws(Exception::class)
    override fun run(args: ApplicationArguments) {
        val sshd = SshServer.setUpDefaultServer()
        sshd.port = 2223
        sshd.keyPairProvider = SimpleGeneratorHostKeyProvider(File("my.pem").toPath())
        sshd.shellFactory = InteractiveProcessShellFactory()
        sshd.commandFactory = ScpCommandFactory()
        sshd.start()
    }
}