import React from "react";
import Form from "react-bootstrap/Form";
import { Col, Button } from "react-bootstrap";
import { Container } from "react-bootstrap";
import Spinner from "react-bootstrap/Spinner";
import { appContext } from "../../appContext";

class ProfileDetails extends React.Component {
    state = { user: null };
    static contextType = appContext;

    async componentDidMount() {
        this.setState({ user: this.context.user });
    }
    render() {
        console.log("working");
        if (this.state.user == null) {
            return (
                <Container className="p-5">
                    <Spinner animation="border" role="status">
                        <span className="sr-only">Loading...</span>
                    </Spinner>
                </Container>
            );
        } else {
            return (
                <Container className="window radius" style={{ width: "750px" }}>
                    <Form style={{ padding: "15px" }}>
                        <Form.Row>
                            <Form.Group as={Col} controlId="formGridEmail">
                                <Form.Label>Email</Form.Label>
                                <Form.Control
                                    type="email"
                                    placeholder="Enter email"
                                    value={this.state.user.email}
                                    disabled
                                />
                            </Form.Group>

                            <Form.Group as={Col} controlId="formGridPassword">
                                <Form.Label>Nové heslo</Form.Label>
                                <Form.Control
                                    type="password"
                                    placeholder="Password"
                                    disabled
                                />
                            </Form.Group>
                        </Form.Row>
                        <Form.Row>
                            <Form.Group as={Col} controlId="formStreet">
                                <Form.Label>Ulica</Form.Label>
                                <Form.Control
                                    placeholder="1234 Main St"
                                    value={this.state.user.address.street}
                                    disabled
                                />
                            </Form.Group>

                            <Form.Group as={Col} controlId="formHouseNumber">
                                <Form.Label>Číslo domu</Form.Label>
                                <Form.Control
                                    placeholder="Apartment, studio, or floor"
                                    value={this.state.user.address.houseNumber}
                                    disabled
                                />
                            </Form.Group>
                        </Form.Row>
                        <Form.Row>
                            <Form.Group as={Col} controlId="formGridCity">
                                <Form.Label>Mesto</Form.Label>
                                <Form.Control
                                    value={this.state.user.address.city}
                                    disabled
                                />
                            </Form.Group>

                            <Form.Group as={Col} controlId="formGridCountry">
                                <Form.Label>Krajina</Form.Label>
                                <Form.Control
                                    value={this.state.user.address.country}
                                    disabled
                                />
                            </Form.Group>

                            <Form.Group as={Col} controlId="formGridZip">
                                <Form.Label>PSČ</Form.Label>
                                <Form.Control
                                    value={this.state.user.address.zipCode}
                                    disabled
                                />
                            </Form.Group>
                        </Form.Row>

                        <Button
                            variant="primary"
                            type="submit"
                            className="submit"
                            disabled
                        >
                            Ulož zmeny
                        </Button>
                    </Form>
                </Container>
            );
        }
    }
}

export default ProfileDetails;
