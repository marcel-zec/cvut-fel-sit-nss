import React from "react";
import { Table } from "react-bootstrap";
import { Container } from "react-bootstrap";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import Spinner from "react-bootstrap/Spinner";
import { Link } from "react-router-dom";
import ButtonInRow from "../../SmartGadgets/ButtonInRow";
import { BASE_API_URL } from "../../../App";
import { appContext } from "../../../appContext";

class Index extends React.Component {
    static contextType = appContext;

    state = {
        categories: null,
    };

    isRole(role) {
        return role == this.context.user.role;
    }

    async componentDidMount() {
        const response = await fetch(BASE_API_URL + "/category", {
            method: "GET",
            mode: "cors",
            credentials: "include",
            headers: {
                "Content-Type": "application/json",
            },
        });
        const data = await response.json();
        console.log(data);
        this.setState({ categories: data });
    }

    render() {
        if (this.state.categories === null) {
            return (
                <Container className="mt-5 p-5">
                    <Spinner animation="border" role="status">
                        <span className="sr-only">Loading...</span>
                    </Spinner>
                </Container>
            );
        } else {
            let tableRows = [];
            if (this.state.categories.length > 0) {
                this.state.categories.forEach((element) => {
                    let content = [<td>{element.name}</td>];
                    if (this.isRole("ADMIN")) {
                        content.push(
                            <td>
                                <Link
                                    className="p-3"
                                    to={"category/" + element.id + "/edit"}
                                >
                                    <FontAwesomeIcon icon="cog" />
                                </Link>

                                <Link className="p-3">
                                    <FontAwesomeIcon icon="trash-alt" />
                                </Link>
                            </td>
                        );
                    }
                    tableRows.push(<tr>{content}</tr>);
                });
            }

            return (
                <Container>
                    {this.isRole("ADMIN") ? (
                        <ButtonInRow
                            variant="success"
                            link="/category/create"
                            side="right"
                            label="Pridať kategóriu"
                        />
                    ) : null}

                    <Table striped bordered hover>
                        <thead>
                            <tr>
                                <th>Meno</th>
                                {this.isRole("ADMIN") ? <th></th> : null}
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
