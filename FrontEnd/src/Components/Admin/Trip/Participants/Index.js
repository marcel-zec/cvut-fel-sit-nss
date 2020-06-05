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
import { Container, Tab, Tabs } from "react-bootstrap";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import Spinner from "react-bootstrap/Spinner";
import { Link, withRouter } from "react-router-dom";
import ButtonInRow from "../../../SmartGadgets/ButtonInRow";
import { appContext } from "../../../../appContext";
import { BASE_API_URL } from "../../../../App";

class Index extends React.Component {
    static contextType = appContext;
    state = { sessions: null };
    async componentDidMount() {
        await fetch(
            BASE_API_URL + "/trip/participants/" + this.props.match.params.id,
            {
                method: "GET",
                credentials: "include",
                headers: {
                    "Content-Type": "application/json",
                },
            }
        )
            .then((response) => {
                if (response.ok) return response.json();
                else console.error(response.status);
            })
            .then((data) => {
                this.setState({ sessions: data });
                console.log(data);
            })
            .catch((error) => {
                console.error(error);
            });
    }

    render() {
        if (this.state.sessions === null) {
            return (
                <Spinner animation="border" role="status">
                    <span className="sr-only">Loading...</span>
                </Spinner>
            );
        } else {
            let tabs = null;
            if (this.state.sessions.length > 0) {
                tabs = [];
                this.state.sessions.forEach((element) => {
                    let rows = [];
                    if (element.enrollments.length > 0) {
                        element.enrollments.forEach((enrollment) => {
                            rows.push(
                                <tr>
                                    <td>
                                        {enrollment.owner.firstName +
                                            " " +
                                            enrollment.owner.lastName}
                                    </td>
                                    <td>{enrollment.owner.email}</td>
                                    <td>{enrollment.owner.address.country}</td>
                                    <td>
                                        {enrollment.owner.travel_journal.level}
                                    </td>
                                </tr>
                            );
                        });
                    }
                    tabs.push(
                        <Tab
                            eventKey={element.session.id}
                            title={element.session.from_date}
                        >
                            <Table striped bordered hover>
                                <thead>
                                    <tr>
                                        <th>User name</th>

                                        <th>Email</th>
                                        <th>Country</th>
                                        <th>Level</th>
                                    </tr>
                                </thead>
                                <tbody>{rows}</tbody>
                            </Table>
                        </Tab>
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
                <Container className="mt-3">
                    <ButtonInRow
                        variant="danger"
                        link={{
                            pathname: "/trip",
                        }}
                        side="left"
                        label=""
                        back={true}
                    />
                    <Tabs
                        defaultActiveKey={
                            this.state.sessions.length > 0
                                ? this.state.sessions[0].id
                                : ""
                        }
                        id="uncontrolled-tab-example"
                    >
                        {tabs}
                    </Tabs>
                </Container>
            );
        }
    }
}

export default withRouter(Index);
