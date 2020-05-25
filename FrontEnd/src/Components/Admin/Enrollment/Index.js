import React from "react";
import { Table, Tooltip, OverlayTrigger } from "react-bootstrap";
import { Container } from "react-bootstrap";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import Spinner from "react-bootstrap/Spinner";
import { Link, withRouter } from "react-router-dom";
import ButtonInRow from "../../SmartGadgets/ButtonInRow";
import ModalCentered from "../../SmartGadgets/ModalCentered";
import MyAlert from "../../SmartGadgets/MyAlert";

class Index extends React.Component {
    state = {
        items: null,
        modal: {
            show: false,
            title: null,
            description: null,
            button: {
                title: "Close",
                onClick: this.submitHandler,
            },
        },
    };

    submitHandler = async (event, enrollment) => {
        await fetch(`http://localhost:8080/enrollment/close/` + enrollment.id, {
            method: "POST",
            mode: "cors",
            credentials: "include",
            headers: {
                "Content-Type": "application/json",
            },
        })
            .then((response) => {
                if (response.ok) {
                    this.onHideModalHandler();
                    this.fetchData();
                    this.props.history.push({
                        alert: <MyAlert text="Trip closed" flash={true} />,
                    });
                } else console.error(response.status);
            })
            .catch((error) => {
                console.error(error);
            });
    };

    fetchData = async () => {
        await fetch(`http://localhost:8080/enrollment/close`, {
            method: "GET",
            mode: "cors",
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
                this.setState({ items: data });
                console.log(data);
            })
            .catch((error) => {
                console.error(error);
            });
    };

    async componentDidMount() {
        await this.fetchData();
    }

    endClickHandler = (enrollment, user) => {
        this.setState({
            modal: {
                show: true,
                title: "End enrollment",
                description: [
                    "Do you want to end trip enrollment with full reward and 5 start rating?",
                    user.firstName + " " + user.lastName,
                    "from " +
                        enrollment.tripSession.from_date +
                        " to " +
                        enrollment.tripSession.to_date,
                ],

                button: {
                    title: "Yes",
                    onClick: this.submitHandler,
                    onClickParameter: enrollment,
                },
            },
        });
    };

    onHideModalHandler = () => {
        this.setState({
            modal: {
                show: false,
                title: null,
                description: null,
                button: {
                    title: "Yes",
                    onClick: this.submitHandler,
                },
            },
        });
    };

    render() {
        if (this.state.items === null) {
            return (
                <Container className="mt-5 p-5">
                    <Spinner animation="border" role="status">
                        <span className="sr-only">Loading...</span>
                    </Spinner>
                </Container>
            );
        } else {
            let tableRows = [];
            if (this.state.items.length > 0) {
                this.state.items.forEach((item) => {
                    tableRows.push(
                        <tr>
                            <td>{item.enrollmentDto.trip.name}</td>
                            <td>
                                {item.owner.firstName +
                                    " " +
                                    item.owner.lastName}
                            </td>
                            <td>{item.enrollmentDto.tripSession.from_date}</td>
                            <td>{item.enrollmentDto.tripSession.to_date}</td>
                            <td>
                                <OverlayTrigger
                                    key="set-end"
                                    overlay={
                                        <Tooltip>
                                            Set reward and review.
                                        </Tooltip>
                                    }
                                >
                                    <Link
                                        className="p-3"
                                        to={"close/" + item.enrollmentDto.id}
                                    >
                                        <FontAwesomeIcon icon="cog" />
                                    </Link>
                                </OverlayTrigger>

                                <OverlayTrigger
                                    key="end"
                                    overlay={
                                        <Tooltip>
                                            End trip with full reward and 5
                                            stars review without notes.
                                        </Tooltip>
                                    }
                                >
                                    <Link
                                        className="p-3"
                                        onClick={() =>
                                            this.endClickHandler(
                                                item.enrollmentDto,
                                                item.owner
                                            )
                                        }
                                    >
                                        <FontAwesomeIcon icon="check-circle" />
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
                alert = this.props.location.alert;
            }

            return (
                <Container>
                    {alert}

                    <ModalCentered
                        show={this.state.modal.show}
                        onHide={() => this.onHideModalHandler()}
                        title={this.state.modal.title}
                        size="lg"
                        description={this.state.modal.description}
                        button={this.state.modal.button}
                    />
                    <Table striped bordered hover>
                        <thead>
                            <tr>
                                <th>Trip</th>
                                <th>User</th>
                                <th>Date of start</th>
                                <th>Date of end</th>
                                <th>Settings</th>
                            </tr>
                        </thead>
                        <tbody>{tableRows}</tbody>
                    </Table>
                </Container>
            );
        }
    }
}

export default withRouter(Index);
