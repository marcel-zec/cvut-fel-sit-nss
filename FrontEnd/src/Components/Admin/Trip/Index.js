import React from "react";
import Form from "react-bootstrap/Form";
import {
    Col,
    Button,
    Row,
    Table,
    OverlayTrigger,
    Tooltip,
} from "react-bootstrap";
import { Container } from "react-bootstrap";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import Spinner from "react-bootstrap/Spinner";
import { Link } from "react-router-dom";
import ButtonInRow from "../../SmartGadgets/ButtonInRow";
import { appContext } from "../../../appContext";
import { BASE_API_URL } from "../../../App";

class Index extends React.Component {
    static contextType = appContext;
    state = { trips: null };
    async componentDidMount() {
        await fetch(BASE_API_URL + "/trip", {
            method: "GET",
            credentials: "include",
            headers: {
                "Content-Type": "application/json",
            },
        })
            .then((response) => {
                if (response.ok) return response.json();
                else console.error(response.status);
            })
            .then((data) => {
                this.setState({ trips: data });
            })
            .catch((error) => {
                console.error(error);
            });
    }

    render() {
        if (this.state.trips === null) {
            return (
                <Spinner animation="border" role="status">
                    <span className="sr-only">Loading...</span>
                </Spinner>
            );
        } else {
            let tableRows = [];
            if (this.state.trips.length > 0) {
                this.state.trips.forEach((element) => {
                    let category = null;
                    if (element.category) category = element.category.name;
                    console.log(element);
                    tableRows.push(
                        <tr>
                            <td>{element.name}</td>
                            <td>{category}</td>
                            <td>{element.required_level}</td>
                            <td>{element.possible_xp_reward}</td>
                            <td>{element.salary}</td>
                            <td>
                                <OverlayTrigger
                                    key="participants"
                                    overlay={
                                        <Tooltip>
                                            Ukáž zapísaných uživateľov.
                                        </Tooltip>
                                    }
                                >
                                    <Link
                                        className="p-3"
                                        to={
                                            "trip/" +
                                            element.short_name +
                                            "/participants"
                                        }
                                    >
                                        <FontAwesomeIcon
                                            icon="address-card"
                                            size="lg"
                                        />
                                    </Link>
                                </OverlayTrigger>

                                <OverlayTrigger
                                    key="edit"
                                    overlay={<Tooltip>Uprav brigádu.</Tooltip>}
                                >
                                    <Link
                                        className="p-3"
                                        to={
                                            "trip/" +
                                            element.short_name +
                                            "/edit"
                                        }
                                    >
                                        <FontAwesomeIcon icon="cog" size="lg" />
                                    </Link>
                                </OverlayTrigger>

                                <Link className="p-3">
                                    <FontAwesomeIcon icon="trash-alt" />
                                </Link>
                            </td>
                        </tr>
                    );
                });
            }

            /**
             * Alert (flash message) from this.props.location.alert
             */
            let alert = null;
            if (
                this.props.location &&
                this.props.location.hasOwnProperty("alert")
            ) {
                console.log(this.props.location);
                alert = this.props.location.alert;
            }

            return (
                <Container>
                    <ButtonInRow
                        variant="success"
                        link="/trip/create"
                        side="right"
                        label="Pridať brigádu"
                    />

                    {alert}

                    <Table striped bordered hover>
                        <thead>
                            <tr>
                                <th>Meno</th>
                                <th>Kategória</th>
                                <th>Požadovaný min. level</th>
                                <th>Odmena XP</th>
                                <th>Výplata</th>
                                <th></th>
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
