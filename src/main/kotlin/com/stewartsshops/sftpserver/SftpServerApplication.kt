package com.stewartsshops.sftpserver

import org.apache.sshd.common.file.virtualfs.VirtualFileSystemFactory
import org.apache.sshd.scp.server.ScpCommandFactory
import org.apache.sshd.server.SshServer
import org.apache.sshd.server.auth.password.PasswordAuthenticator
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider
import org.apache.sshd.server.shell.InteractiveProcessShellFactory
import org.apache.sshd.server.subsystem.SubsystemFactory
import org.apache.sshd.sftp.server.SftpSubsystemFactory
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Component
import java.io.File
import java.nio.file.Path

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

        sshd.fileSystemFactory = VirtualFileSystemFactory(Path.of("/opt/pos"))

        sshd.passwordAuthenticator = PasswordAuthenticator { username, password, _ ->
            username == "admin" && password == "admin"
        }

        sshd.subsystemFactories = mutableListOf<SubsystemFactory>(SftpSubsystemFactory())

        sshd.start()
    }
}