import React from "react";
import Form from "react-bootstrap/Form";
import { Col, Button, Row, Table, Tab, Tabs } from "react-bootstrap";
import { Container } from "react-bootstrap";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import Spinner from "react-bootstrap/Spinner";
import { Link, withRouter } from "react-router-dom";
import ButtonInRow from "../../SmartGadgets/ButtonInRow";
import { BASE_API_URL } from "../../../App";

class Index extends React.Component {
    state = {
        categorized: null,
        certificate: null,
        special: null,
        type: null,
    };

    async componentDidMount() {
        console.log("location");
        console.log(this.props.location);

        if (this.props.location) {
            if (this.props.location.hasOwnProperty("type"))
                this.setState({ type: this.props.location.type });
        }

        const requestSettings = {
            method: "GET",
            mode: "cors",
            credentials: "include",
            headers: {
                "Content-Type": "application/json",
            },
        };

        const response = await fetch(
            BASE_API_URL + "/achievement/categorized",
            requestSettings
        );
        const data = await response.json();
        console.log(data);
        this.setState({ categorized: data });

        const response1 = await fetch(
            BASE_API_URL + "/achievement/certificate",
            requestSettings
        );
        const data1 = await response1.json();
        console.log(data1);
        this.setState({ certificate: data1 });

        const response2 = await fetch(
            BASE_API_URL + "/achievement/special",
            requestSettings
        );
        const data2 = await response2.json();
        console.log(data2);
        this.setState({ special: data2 });
    }

    render() {
        if (
            this.state.categorized === null ||
            this.state.certificate === null ||
            this.state.special === null
        ) {
            return (
                <Container className="mt-5 p-5">
                    <Spinner animation="border" role="status">
                        <span className="sr-only">Loading...</span>
                    </Spinner>
                </Container>
            );
        } else {
            let categorizedRows = [];
            if (this.state.categorized.length > 0) {
                this.state.categorized.forEach((element) => {
                    categorizedRows.push(
                        <tr>
                            <td>{element.name}</td>
                            <td>
                                <FontAwesomeIcon
                                    icon={element.icon}
                                    size="3x"
                                />
                            </td>
                            <td>{element.category.name}</td>
                            <td>{element.limit}</td>
                            <td>
                                <Link
                                    className="p-3"
                                    to={{
                                        pathname:
                                            "achievement/" +
                                            element.id +
                                            "/edit",
                                        type: "categorized",
                                    }}
                                >
                                    <FontAwesomeIcon icon="cog" />
                                </Link>

                                <Link className="p-3">
                                    <FontAwesomeIcon icon="trash-alt" />
                                </Link>
                            </td>
                        </tr>
                    );
                });
            }

            let specialRows = [];
            if (this.state.special.length > 0) {
                this.state.special.forEach((element) => {
                    specialRows.push(
                        <tr>
                            <td>{element.name}</td>
                            <td>
                                <FontAwesomeIcon
                                    icon={element.icon}
                                    size="3x"
                                />
                            </td>
                            <td>
                                <Link
                                    className="p-3"
                                    to={{
                                        pathname:
                                            "achievement/" +
                                            element.id +
                                            "/edit",
                                        type: "special",
                                    }}
                                >
                                    <FontAwesomeIcon icon="cog" />
                                </Link>

                                <Link className="p-3">
                                    <FontAwesomeIcon icon="trash-alt" />
                                </Link>
                            </td>
                        </tr>
                    );
                });
            }

            let certificateRows = [];
            if (this.state.certificate.length > 0) {
                this.state.certificate.forEach((element) => {
                    certificateRows.push(
                        <tr>
                            <td>{element.name}</td>
                            <td>
                                <FontAwesomeIcon
                                    icon={element.icon}
                                    size="3x"
                                />
                            </td>
                            <td>
                                <Link
                                    className="p-3"
                                    to={{
                                        pathname:
                                            "achievement/" +
                                            element.id +
                                            "/edit",
                                        type: "certificate",
                                    }}
                                >
                                    <FontAwesomeIcon icon="cog" />
                                </Link>

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
                alert = this.props.location.alert;
            }

            return (
                <Container>
                    <ButtonInRow
                        variant="success"
                        link="/achievement/create"
                        side="right"
                        label="Pridať achievement"
                    />

                    {alert}

                    <Tabs
                        defaultActiveKey={
                            this.state.type ? this.state.type : "special"
                        }
                        id="uncontrolled-tab-example"
                    >
                        <Tab eventKey="special" title="Brigádne">
                            <Table striped bordered hover>
                                <thead>
                                    <tr>
                                        <th>Meno</th>
                                        <th>Ikona</th>
                                        <th></th>
                                    </tr>
                                </thead>
                                <tbody>{specialRows}</tbody>
                            </Table>
                        </Tab>
                        <Tab eventKey="categorized" title="Kategorizované">
                            <Table striped bordered hover>
                                <thead>
                                    <tr>
                                        <th>Meno</th>
                                        <th>Ikona</th>
                                        <th>Kategória</th>
                                        <th>Počet brigád</th>
                                        <th></th>
                                    </tr>
                                </thead>
                                <tbody>{categorizedRows}</tbody>
                            </Table>
                        </Tab>
                        <Tab eventKey="certificate" title="Certifikáty">
                            <Table striped bordered hover>
                                <thead>
                                    <tr>
                                        <th>Meno</th>
                                        <th>Ikona</th>
                                        <th></th>
                                    </tr>
                                </thead>
                                <tbody>{certificateRows}</tbody>
                            </Table>
                        </Tab>
                    </Tabs>
                </Container>
            );
        }
    }
}

export default withRouter(Index);
