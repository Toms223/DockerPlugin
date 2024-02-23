import com.dorckercord.dockerplugin.requests.DockerCordAPIClient
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow

class DockerCordAPIClientTests {
    private val dockerClient = DockerCordAPIClient("localhost:3030")
    @Test
    fun `should return players successfully`(){
        assertDoesNotThrow {
            dockerClient.players
        }
    }

    @Test
    fun `should return instances successfully`(){
        assertDoesNotThrow {
            dockerClient.instances
        }
    }

}