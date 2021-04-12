package service.parser;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ParserController {
    /**
     * To get health of the app
     *
     * @return ResponseEntity<String>  - Response entity with HTTP status code
     */
    @GetMapping("/health")
    public ResponseEntity<String> getHealth() {
        return new ResponseEntity<String>("Server was started", HttpStatus.OK);
    }

    @RestController
    public class KeywordController {
        @RequestMapping("/keyword")
        public String keyword(@RequestParam(value = "value") String keyword) {
            return new Keyword(keyword).getKeyword();
        }
    }

    @RestController
    public class GetResultsController {
        @RequestMapping("/getResults")
        public String keyword(@RequestParam(value = "value") String keyword) {
            return new GetResults(keyword).getResults();
        }
    }

    @RestController
    public class UploadController {
        @RequestMapping("/upload")
        public String keyword(@RequestParam(value = "myParam") String[] myParams) {
            if (myParams.length == 0) {
                return "error";
            } else {
                for (String myParam : myParams) {
                    ParserThread parser = new ParserThread();
                    parser.setStr(myParam);
                    parser.start();
                    try {
                        parser.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                return "success";
            }
        }
    }
}
