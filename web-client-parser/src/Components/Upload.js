import React, { useState } from 'react';
import { Button, Container, Form, Row, Col} from 'react-bootstrap'
import Server from '../Services/Server.js'

const Upload = () => {
    const [file, setFile] = useState(0);
    let fileReader;


    const handleFileRead = () => {
        let array = file.split('\n');
        console.log(array);
        Server.uploadCSV(array).then(res =>{
            if(res === 'success'){
                
            }else{

            }
        });
    };

    const handleFileChosen = (fileUpload) => {
        console.log(fileUpload);
        fileReader = new FileReader();
        fileReader.onloadend = () => {
            const content = fileReader.result;
            setFile(content);
        };
        fileReader.readAsText(fileUpload);

    };

    return <>
        <Container>
            <Row className="justify-content-md-center my-5">
                <Col>
                    <Form className="text-center">
                        <Form.Label><h2>Виберіть .csv файл для завантаження</h2></Form.Label>
                        <Form.Group>
                            <Form.File type="file"
                                id="inputGroupFile01"
                                label="Виберіть .csv файл"
                                accept='.csv'
                                onChange={e => handleFileChosen(e.target.files[0])}
                                custom />
                        </Form.Group>
                        <Form.Text className="text-muted">
                            Файл з ключовими словами для парсингу
                    </Form.Text>
                    </Form>
                </Col>
            </Row>
            <Row>
                <Col></Col>
                <Col>
                    <Button variant="primary" size="lg" onClick={handleFileRead} block>
                        Завантажити
                </Button>
                </Col>
                <Col></Col>
            </Row>
        </Container>
    </>
};
export default Upload;