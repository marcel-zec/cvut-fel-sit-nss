import React from "react";
import Form from "react-bootstrap/Form";
import { Col, Button, Row, Table } from "react-bootstrap";
import { Container } from "react-bootstrap";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import Spinner from "react-bootstrap/Spinner";
import { Link } from "react-router-dom";
import ButtonInRow from "../../SmartGadgets/ButtonInRow";

class Index extends React.Component {
    state = { users: null };
    async componentDidMount() {
        fetch("http://localhost:8080/user", {
            method: "GET",
            mode: "cors",
            credentials: "include",
            headers: {
                "Content-Type": "application/json",
            },
        })
            .then((response) => {
                if (response.ok) return response.json();
                else throw Error(response.status);
            })
            .then((data) => {
                this.setState({ users: data });
                console.log(data);
            })
            .catch((error) => {
                console.error(error);
            });
    }

    render() {
        if (this.state.users === null) {
            return (
                <Container className="mt-5 p-5">
                    <Spinner animation="border" role="status">
                        <span className="sr-only">Loading...</span>
                    </Spinner>
                </Container>
            );
        } else {
            let tableRows = [];
            if (this.state.users.length > 0) {
                this.state.users.forEach((element) => {
                    tableRows.push(
                        <tr>
                            <td>{element.firstName}</td>
                            <td>{element.lastName}</td>
                            <td>{element.email}</td>
                            <td>{element.address.country}</td>
                            <td>{element.address.city}</td>
                            <td>{element.address.zipCode}</td>
                            <td>{element.address.street}</td>
                            <td>{element.address.houseNumber}</td>
                            {/*<td>
                                <Link
                                    className="p-3"
                                    to={"/user/" + element.id}
                                >
                                    <FontAwesomeIcon icon="cog" />
                                </Link>
                            </td>*/}
                        </tr>
                    );
                });
            }

            return (
                <Container>
                    <Table striped bordered hover>
                        <thead>
                            <tr>
                                <th>First name</th>
                                <th>Last name</th>
                                <th>E-mail</th>
                                <th>Country</th>
                                <th>City</th>
                                <th>Zipcode</th>
                                <th>Street</th>
                                <th>House number</th>
                                {/*<th>Settings</th>*/}
                            </tr>
                        </thead>
                        <tbody>{tableRows}</tbody>
                    </Table>
                </Container>
            );
        }
    }
}

export default Index;
