package com.newswhip.UrlDB

import com.newswhip.UrlDB.component.UrlDBRunner
import com.newswhip.UrlDB.repository.UrlRepository
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

@SpringBootTest
class UrlDBRunnerSpec extends Specification {
    def repository = Mock(UrlRepository)

    @Shared
    def runner = new UrlDBRunner();

    void setup() {
        runner.urlRepository = repository
    }

    @Unroll
    def "ExecuteCommand - invalid command"() {
        when:
        def buffer = new ByteArrayOutputStream()
        System.out = new PrintStream(buffer)

        and:
        runner.ExecuteCommand(command)

        then:
        buffer.toString() == message
        notThrown()

        where:
        command                 | message
        "ABC"                   | 'Invalid command\n'
        "ADD"                   | 'Invalid command format\n'
        "ADD www.google.com"    | 'Invalid command format\n'
        "ADD www.google.com aa" | 'Invalid URL or Score\n'
        "REMOVE"                | 'Invalid command format\n'
    }

    def "ExecuteCommand - ADD"() {
        when:
        runner.ExecuteCommand("ADD www.google.com 100")

        then:
        1 * repository.save(_)
        notThrown()
    }

    def "ExecuteCommand - Remove"() {
        when:
        runner.ExecuteCommand("REMOVE www.google.com")

        then:
        1 * repository.deleteByUrl(_)
        notThrown()
    }

    def "ExecuteCommand - EXPORT"() {
        when:
        def buffer = new ByteArrayOutputStream()
        System.out = new PrintStream(buffer)

        and:
        runner.ExecuteCommand("EXPORT")

        then:
        1 * repository.findAll() >> null
        buffer.toString() == 'No record\n'
    }
}
