import React from "react";
import Form from "react-bootstrap/Form";
import { Col, Button } from "react-bootstrap";
import { Container } from "react-bootstrap";
import Spinner from "react-bootstrap/Spinner";
import { appContext } from "../../appContext"

class ProfileDetails extends React.Component {
    state = {user : null};
    static contextType = appContext;

    async componentDidMount() {
        this.setState({user:this.context.user});
    }
    render() {
        console.log("working");
        if(this.state.user == null){
            return <Container className="p-5">
            <Spinner animation="border" role="status">
                <span className="sr-only">Loading...</span>
            </Spinner>
        </Container>;
        }else{
            return (
                <Container className="window radius" style={{width:"750px"}}>
                    <Form style={{padding:"15px"}}>
                        <Form.Row>
                            <Form.Group as={Col} controlId="formGridEmail">
                                <Form.Label>Email</Form.Label>
                                <Form.Control
                                    type="email"
                                    placeholder="Enter email"
                                    value={this.state.user.email}
                                />
                            </Form.Group>

                            <Form.Group as={Col} controlId="formGridPassword">
                                <Form.Label>Password</Form.Label>
                                <Form.Control
                                    type="password"
                                    placeholder="Password"
                                />
                            </Form.Group>
                        </Form.Row>
                        <Form.Row>
                            <Form.Group as={Col} controlId="formStreet">
                                <Form.Label>Street</Form.Label>
                                <Form.Control placeholder="1234 Main St" 
                                    value={this.state.user.address.street}
                                />
                            </Form.Group>

                            <Form.Group as={Col} controlId="formHouseNumber">
                                <Form.Label>House number</Form.Label>
                                <Form.Control placeholder="Apartment, studio, or floor" value={this.state.user.address.houseNumber} />
                            </Form.Group>
                        </Form.Row>
                        <Form.Row>
                            <Form.Group as={Col} controlId="formGridCity" >
                                <Form.Label>City</Form.Label>
                                <Form.Control value={this.state.user.address.city} />
                            </Form.Group>

                            <Form.Group as={Col} controlId="formGridCountry" >
                                <Form.Label>Country</Form.Label>
                                <Form.Control value={this.state.user.address.country} />
                            </Form.Group>

                            <Form.Group as={Col} controlId="formGridZip">
                                <Form.Label>Zip</Form.Label>
                                <Form.Control value={this.state.user.address.zipCode} />
                            </Form.Group>
                        </Form.Row>

                        <Button variant="primary" type="submit" className="submit">
                            Confirm changes
                        </Button>
                    </Form>
                </Container>
            );
            }
    }
}

export default ProfileDetails;
