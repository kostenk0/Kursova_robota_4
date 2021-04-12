import React, { Component } from 'react'
import { Button, Container, Form, Row, Col, Table } from 'react-bootstrap'
import Server from '../Services/Server.js'

export default class Find extends Component {
    constructor(props) {
        super(props);
        this.getUserKeyword = this.getUserKeyword.bind(this);
        this.sendRequest = this.sendRequest.bind(this);

        this.state = {
            keyword: '',
            status: true,
            results: ""
        };
    }

    getUserKeyword(e) {
        this.setState({
            keyword: e.target.value
        });
    }
    sendRequest(e) {
        e.preventDefault();
        this.setState({
            status: false
        });
        Server.health().then(res => {
            if (res === 200) {
                Server.getResults(this.state.keyword).then(res => {
                    console.log(res);
                    this.setState({
                        status: true,
                        results: res
                    });
                });
            } else {

            }
        });
    }

    render() {
        return (
                <Container>
                    <Row className="justify-content-md-center my-5" >
                        <Col>
                            <Form className="text-center">
                                <Form.Group>
                                    <Form.Label ><h2>Пошук в базі даних</h2></Form.Label>
                                    <Form.Control size="lg" type="text" placeholder="Введіть ключове слово"
                                        value={this.state.keyword} onChange={this.getUserKeyword} />
                                    <Form.Text className="text-muted">
                                        Ключове слово - це слово або фраза, яка буде використана для пошуку.
                                    </Form.Text>
                                </Form.Group>
                            </Form>
                        </Col>
                    </Row>
                    <Row className="justify-content-md-center">
                        <Col></Col>
                        <Col>
                            <Button variant="success" disabled={!this.state.status} onClick={this.state.status ? this.sendRequest : null} size="lg" block>
                                {this.state.status ? 'Знайти' : 'Шукаємо. Зачекайте...'}
                            </Button>
                        </Col>
                        <Col></Col>
                    </Row>
                    <Row>
                        <Col>
                        {
                            this.state.results ?
                            <Table striped bordered hover variant="dark" className="my-3" size="sm" responsive>
                                <thead>
                                    <tr>
                                        <th>Позиція</th>
                                        <th>Дата</th>
                                        <th>URL + snippet</th>
                                    </tr>
                                </thead>
                                <tbody>
                                {
                                this.state.results ? this.state.results.map((result) =>
                                    <tr>
                                        <td>{result.position}</td>
                                        <td>{result.datetime.substr(0,16)}</td>
                                            <tr style={{color: 'yellow'}}>
                                            {result.url}
                                            </tr>
                                            <tr style={{color: 'lightgreen'}}>
                                            {result.snippet}
                                            </tr>
                                    </tr>
                                     ) : null
                                    }
                                </tbody>
                            </Table>
                            : null
                        }
                        </Col>
                    </Row>
                </Container>
        );
    };
}