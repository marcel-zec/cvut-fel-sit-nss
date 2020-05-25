import React from "react";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { Card, Button, Col, Form, Image } from "react-bootstrap";
import { Link } from "react-router-dom";
import OverlayTrigger from "react-bootstrap/OverlayTrigger";
import Tooltip from "react-bootstrap/Tooltip";
import AchievmentModal from "../../SmartGadgets/AchievementModal";
import DatePicker from "react-datepicker";
import RangeSlider from "react-bootstrap-range-slider";
import "react-bootstrap-range-slider/dist/react-bootstrap-range-slider.css";
import ModalCentered from "../../SmartGadgets/ModalCentered";
import { BASE_API_URL } from "../../../App";

class TripHistory extends React.Component {
    state = {
        enrollment: null,
        show_review: false,
        show_modal_review: false,
        note: null,
        rating: 5,
    };
    async componentDidMount() {
        this.setState({ enrollment: this.props.trip });
    }

    toggleReviewForm = () => {
        this.setState({ show_review: !this.state.show_review });
    };

    showModalHandler = () => {
        const newState = { ...this.state };
        newState.show_modal_review = true;
        this.setState(newState);
    };

    onHideModalHandler = () => {
        const newState = { ...this.state };
        newState.show_modal_review = false;
        this.setState(newState);
    };

    submitHandler = (event) => {
        event.preventDefault();
        const request = {
            note: this.state.note,
            rating: this.state.rating,
        };
        fetch(BASE_API_URL + "/trip_review/" + this.state.enrollment.id, {
            method: "POST",
            credentials: "include",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(request),
        })
            .then((response) => {
                if (response.ok) {
                    this.props.refreshFunction();
                } else console.error(response.status);
            })
            .catch((error) => {
                console.error(error);
            });
    };

    inputUpdateHandler = (event, inputName) => {
        console.log(this.state);
        if (inputName == "note") {
            if (event.target.value.trim() == "") {
                this.setState({ note: null });
            } else {
                this.setState({ note: event.target.value });
            }
        } else if (inputName == "rating") {
            this.setState({ rating: Number(event.target.value) });
        }
        console.log(this.state);
    };

    render() {
        if (this.state.enrollment == null) {
            return "null";
        } else {
            const achievments = [];
            console.log(this.state);
            this.state.enrollment.trip.gain_achievements_special.forEach(
                (element) => {
                    achievments.push(
                        <AchievmentModal
                            icon={element.icon}
                            title={element.title}
                            description={element.description}
                            key={element.title}
                        />
                    );
                }
            );
            //incrementing number for tooltips ID
            let placement = 0;
            let commentButton = null;
            //pokud user jeste nenapsal review
            if (
                !this.state.enrollment.hasOwnProperty("tripReview") &&
                !this.state.show_review
            ) {
                //comment icons for new comment
                commentButton = (
                    <OverlayTrigger
                        placement="right"
                        overlay={
                            <Tooltip id={`tooltip-${++placement}`}>
                                Add review of this trip.
                            </Tooltip>
                        }
                    >
                        <Button
                            className="submit createReview"
                            onClick={() => {
                                this.toggleReviewForm();
                            }}
                        >
                            Add review
                            <FontAwesomeIcon icon="comment-medical" />
                        </Button>
                    </OverlayTrigger>
                );
            } else if (
                this.state.show_review &&
                !this.state.enrollment.hasOwnProperty("tripReview")
            ) {
                commentButton = "Review in process";
            } else {
                //comment icons for updating
                commentButton = (
                    <>
                        <Button
                            className="submit createReview"
                            onClick={() => {
                                this.showModalHandler();
                            }}
                        >
                            Show review
                        </Button>
                        <ModalCentered
                            show={this.state.show_modal_review}
                            onHide={() => this.onHideModalHandler()}
                            title={this.state.enrollment.trip.name + " review"}
                            stars={this.state.enrollment.tripReview.rating}
                            description={this.state.enrollment.tripReview.note}
                            size="lg"
                        />
                    </>
                );
            }

            let review = null;
            if (this.state.show_review) {
                review = (
                    <Card className="userTrip window radius mb-5 addReviewForm">
                        <Form onSubmit={(event) => this.submitHandler(event)}>
                            <Card.Body>
                                <Button
                                    className="submit closeButton"
                                    onClick={() => {
                                        this.toggleReviewForm();
                                    }}
                                >
                                    <FontAwesomeIcon icon="times" />
                                </Button>
                                <Form.Label>
                                    <h5>Add review</h5>
                                </Form.Label>
                                <Form.Group
                                    className="d-flex flex-column w-50"
                                    style={{ margin: "auto" }}
                                >
                                    <Form.Label>
                                        Evaluate the trip (5=max,0=min)
                                    </Form.Label>
                                    <RangeSlider
                                        value={this.state.rating}
                                        min={0}
                                        max={5}
                                        step={0.5}
                                        tooltip="auto"
                                        tooltipPlacement="top"
                                        onChange={(event) =>
                                            this.inputUpdateHandler(
                                                event,
                                                "rating"
                                            )
                                        }
                                    />
                                </Form.Group>
                                <Form.Group>
                                    <Form.Control
                                        as="textarea"
                                        rows="2"
                                        placeholder="Please, write some note..."
                                        onChange={(event) =>
                                            this.inputUpdateHandler(
                                                event,
                                                "note"
                                            )
                                        }
                                    />
                                </Form.Group>
                                <Button
                                    variant="primary"
                                    type="submit"
                                    className="mb-1 submit"
                                >
                                    Submit
                                </Button>
                            </Card.Body>
                        </Form>
                    </Card>
                );
            }

            return (
                <>
                    <Card
                        className={
                            "userTrip window radius" +
                            (this.state.show_review ? "" : " mb-3")
                        }
                    >
                        <Card.Body className="d-flex flex-row">
                            <Col xs={2}>
                                <div className="tripImage">
                                    <Image
                                        src="https://www.transparency.cz/wp-content/uploads/Jablonec-nad-Nisou-621x466.jpg"
                                        rounded
                                    />
                                </div>
                            </Col>
                            <Col xs={3}>
                                <Card.Title className="mb-2 text-muted">
                                    Name
                                </Card.Title>
                                <Card.Text>
                                    {this.state.enrollment.trip.name}
                                </Card.Text>

                                <Card.Title className="mb-2 text-muted">
                                    Location
                                </Card.Title>
                                <Card.Text>
                                    {this.state.enrollment.trip.location}
                                </Card.Text>
                            </Col>
                            <Col>
                                <Card.Title className="mb-2 text-muted">
                                    Trip session
                                </Card.Title>
                                <Card.Text className="d-flex">
                                    <DatePicker
                                        className="form-control"
                                        selected={Date.parse(
                                            this.state.enrollment.tripSession
                                                .from_date
                                        )}
                                        dateFormat="dd. MM. yyyy"
                                        disabled={true}
                                    />
                                    <DatePicker
                                        className="form-control"
                                        selected={Date.parse(
                                            this.state.enrollment.tripSession
                                                .to_date
                                        )}
                                        dateFormat="dd. MM. yyyy"
                                        disabled={true}
                                    />
                                </Card.Text>
                                <Card.Title className="mb-2 text-muted">
                                    Reward XP and achievements
                                </Card.Title>
                                <Card.Text>
                                    {this.props.trip.xp} XP - {achievments}
                                </Card.Text>
                            </Col>
                            <Col>
                                <Card.Title className="mb-2 text-muted">
                                    Added to travel journal on
                                </Card.Title>
                                <Card.Text>
                                    <DatePicker
                                        className="form-control"
                                        selected={Date.parse(
                                            this.state.enrollment.enrollDate
                                        )}
                                        dateFormat="dd. MM. yyyy, hh:mm"
                                        disabled={true}
                                    />
                                </Card.Text>
                                <Card.Title className="mb-2 text-muted">
                                    Review
                                </Card.Title>
                                <Card.Text>{commentButton}</Card.Text>
                            </Col>
                        </Card.Body>
                    </Card>
                    {review}
                </>
            );
        }
    }
}

export default TripHistory;
