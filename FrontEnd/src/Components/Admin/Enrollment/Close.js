import React from "react";
import Form from "react-bootstrap/Form";
import {
    Col,
    Button,
    Row,
    Spinner,
    Card,
    InputGroup,
    ListGroup,
    ListGroupItem,
} from "react-bootstrap";
import { Container } from "react-bootstrap";
import { withRouter } from "react-router-dom";
import {
    formValidation,
    validationFeedback,
    validationClassName,
} from "../../../Validator";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import MyAlert from "../../SmartGadgets/MyAlert";
import DatePicker from "react-datepicker";
import Slider from "@material-ui/core/Slider";
import NumericInput from "react-numeric-input";
import RangeSlider from "react-bootstrap-range-slider";
import "react-bootstrap-range-slider/dist/react-bootstrap-range-slider.css";
import { BASE_API_URL } from "../../../App";

class Close extends React.Component {
    state = {
        form: {
            isValid: false,
            elements: {
                actual_xp_reward: {
                    more: false,
                    less: false,
                },
            },
        },
        enrollment: null,
        user: null,

        review: {
            note: null,
            rating: 5,
        },
    };

    /**
     * Update state from input.
     * @param {event} event
     * @param {String} inputName
     * @param {String} stateName
     */
    inputUpdateHandler = async (event, inputName) => {
        console.log("inputhandaler");
        if (inputName == "actual_xp_reward") {
            const newFormState = { ...this.state.form };
            const newEnrollmentState = { ...this.state.enrollment };
            newEnrollmentState["actual_xp_reward"] = Number(event.target.value);
            newFormState.elements.actual_xp_reward["more"] = false;
            newFormState.elements.actual_xp_reward["less"] = false;
            this.setState({
                form: newFormState,
                enrollment: newEnrollmentState,
            });

            if (
                event.target.value >
                this.state.enrollment.trip.possible_xp_reward
            ) {
                newFormState.elements.actual_xp_reward["more"] = true;
                newFormState.elements.actual_xp_reward["less"] = false;
                this.setState({ form: newFormState });
            } else if (event.target.value < 0) {
                newFormState.elements.actual_xp_reward["more"] = false;
                newFormState.elements.actual_xp_reward["less"] = true;
                this.setState({ form: newFormState });
            }
            console.log(this.state);
        } else if (inputName == "recieved_achievements_special") {
            const newEnrollmentState = { ...this.state.enrollment };
            //remove if found
            let foundIndex = newEnrollmentState.recieved_achievements_special.findIndex(
                (item) => item.id == event.target.value
            );
            if (foundIndex > -1) {
                newEnrollmentState.recieved_achievements_special.splice(
                    foundIndex,
                    1
                );
            } else {
                //add if not found
                let foundIndexGained = this.state.enrollment.trip.gain_achievements_special.findIndex(
                    (item) => item.id == event.target.value
                );
                if (foundIndexGained > -1)
                    newEnrollmentState.recieved_achievements_special.push(
                        this.state.enrollment.trip.gain_achievements_special[
                            foundIndexGained
                        ]
                    );
            }
            this.setState({
                enrollment: newEnrollmentState,
            });
        } else if (inputName == "rating") {
            console.log("input rating");
            const newState = { ...this.state.review };

            newState.rating = event.target.value;
            console.log(event.target.value);
            await this.setState({ review: newState });
            console.log(this.state.review);
        } else if (inputName == "note") {
            const newState = { ...this.state.review };

            if (event.target.value.trim() == "") {
                newState.note = null;
            } else {
                newState.note = event.target.value;
            }

            await this.setState({ review: newState });
        }
    };

    validateForm = async () => {
        const newState = { ...this.state.form };
        // formValidation(newState, this.state.trip);
        await this.setState({ form: newState });
    };

    submitHandler = async (event) => {
        event.preventDefault();
        await this.validateForm();
        const request = {
            userReview: { ...this.state.review },
            enrollmentDto: { ...this.state.enrollment },
            tripSessionId: this.state.enrollment.tripSession.id,
        };
        console.log(request);
        ///*if (this.state.form.isValid) {

        fetch(BASE_API_URL + "/enrollment/close", {
            method: "PATCH",
            credentials: "include",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(request),
        }).then((response) => {
            if (response.ok) this.props.history.push("/close");
            //TODO - osetrenie vynimiek
            else console.log("Error: somethhing goes wrong");
        });
        // }*/
    };

    async componentDidMount() {
        const response = await fetch(
            BASE_API_URL + "/enrollment/close/" + this.props.match.params.id,
            {
                method: "GET",
                mode: "cors",
                credentials: "include",
                headers: {
                    "Content-Type": "application/json",
                },
            }
        );
        const data = await response.json();

        console.log(data);

        const user = { ...data.owner };
        const enrollment = { ...data.enrollmentDto };

        enrollment.actual_xp_reward = enrollment.trip.possible_xp_reward;
        for (
            let i = 0;
            i < enrollment.trip.gain_achievements_special.length;
            i++
        ) {
            enrollment.recieved_achievements_special.push(
                enrollment.trip.gain_achievements_special[i]
            );
        }

        this.setState({
            user: user,
            enrollment: enrollment,
        });
    }

    render() {
        if (this.state.enrollment == null || this.state.user == null) {
            return (
                <Container className="p-5 mt-5">
                    <Spinner animation="border" role="status">
                        <span className="sr-only">Loading...</span>
                    </Spinner>
                </Container>
            );
        } else {
            let achievementArray = [];
            if (
                this.state.enrollment.trip.gain_achievements_special.length > 0
            ) {
                this.state.enrollment.trip.gain_achievements_special.forEach(
                    (item) => {
                        achievementArray.push(
                            <Form.Check.Label>
                                <Form.Check.Input
                                    type="checkbox"
                                    defaultChecked={true}
                                    value={item.id}
                                    onChange={(event) =>
                                        this.inputUpdateHandler(
                                            event,
                                            "recieved_achievements_special"
                                        )
                                    }
                                />
                                <FontAwesomeIcon
                                    icon={item.icon}
                                    size="lg"
                                    className="mr-3 ml-3"
                                />
                                {item.name}
                            </Form.Check.Label>
                        );
                    }
                );
            }
            let achievements = (
                <div className="d-flex flex-column align-items-start">
                    {achievementArray}
                </div>
            );

            let achievements_removed = [];
            if (
                this.state.enrollment.trip.gain_achievements_special.length > 0
            ) {
                this.state.enrollment.trip.gain_achievements_special.forEach(
                    (shouldGain) => {
                        if (
                            !this.state.enrollment.recieved_achievements_special.find(
                                (notCanceled) => notCanceled.id == shouldGain.id
                            )
                        ) {
                            achievements_removed.push(shouldGain.name);
                        }
                    }
                );
            }
            let achievement_alert =
                achievements_removed.length > 0 ? (
                    <MyAlert
                        variant="warning"
                        text="Removed achievements"
                        list={achievements_removed}
                    />
                ) : null;

            let xp_reward_alert = null;
            if (this.state.form.elements.actual_xp_reward.less) {
                xp_reward_alert = (
                    <MyAlert variant="danger" text="Cannot be less than 0." />
                );
            } else if (this.state.form.elements.actual_xp_reward.more) {
                xp_reward_alert = (
                    <MyAlert
                        variant="danger"
                        text={
                            "Cannot be more than " +
                            this.state.enrollment.trip.possible_xp_reward +
                            "."
                        }
                    />
                );
            } else if (
                this.state.enrollment.trip.possible_xp_reward >
                this.state.enrollment.actual_xp_reward
            ) {
                xp_reward_alert = (
                    <MyAlert
                        variant="warning"
                        text={
                            "Reward was decresed by " +
                            (this.state.enrollment.trip.possible_xp_reward -
                                this.state.enrollment.actual_xp_reward) +
                            " points."
                        }
                    />
                );
            }

            return (
                <>
                    <Container className="mt-3">
                        <Card /*style={{ width: "18rem" }}*/>
                            <Card.Body>
                                <Card.Subtitle className="text-muted mb-3">
                                    Trip
                                </Card.Subtitle>
                                <Card.Title>
                                    {this.state.enrollment.trip.name}
                                </Card.Title>
                                <Card.Text>
                                    <FontAwesomeIcon
                                        icon="map-marker-alt"
                                        className="mr-3"
                                    />
                                    {this.state.enrollment.trip.location}
                                </Card.Text>
                                <Card.Text>
                                    <FontAwesomeIcon
                                        icon="calendar-alt"
                                        className="mr-3"
                                    />
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
                                        className="form-control ml-3"
                                        selected={Date.parse(
                                            this.state.enrollment.tripSession
                                                .to_date
                                        )}
                                        dateFormat="dd. MM. yyyy"
                                        disabled={true}
                                    />
                                </Card.Text>
                                <Card.Text>
                                    {this.state.enrollment.trip.description}
                                </Card.Text>
                            </Card.Body>
                        </Card>
                    </Container>

                    <Container className="mt-3">
                        <Card>
                            <Card.Body>
                                <Card.Subtitle className="text-muted mb-3">
                                    User reward
                                </Card.Subtitle>
                                <Card.Text>
                                    <FontAwesomeIcon
                                        icon="user-alt"
                                        className="mr-3"
                                    />
                                    {this.state.user.firstName +
                                        " " +
                                        this.state.user.lastName}
                                </Card.Text>
                            </Card.Body>

                            <Form
                                onSubmit={(event) => this.submitHandler(event)}
                            >
                                <Row className="d-flex">
                                    <Card.Body>
                                        <Card.Title className="text-muted">
                                            Gain achievements
                                        </Card.Title>
                                        <Form.Group
                                            as={Col}
                                            className="d-flex flex-column align-items-center"
                                        >
                                            {achievements}
                                        </Form.Group>
                                        {achievement_alert}
                                    </Card.Body>
                                    <Card.Body>
                                        <Form.Group as={Col}>
                                            <Form.Label>XP</Form.Label>
                                            <InputGroup className="mb-3">
                                                <InputGroup.Prepend>
                                                    <InputGroup.Text id="basic-addon1">
                                                        Max{" "}
                                                        {
                                                            this.state
                                                                .enrollment.trip
                                                                .possible_xp_reward
                                                        }
                                                    </InputGroup.Text>
                                                </InputGroup.Prepend>
                                                <Form.Control
                                                    type="number"
                                                    placeholder="XP reward"
                                                    defaultValue={
                                                        this.state.enrollment
                                                            .trip
                                                            .possible_xp_reward
                                                    }
                                                    onInput={(event) =>
                                                        this.inputUpdateHandler(
                                                            event,
                                                            "actual_xp_reward",
                                                            "enrollment"
                                                        )
                                                    }
                                                />
                                            </InputGroup>
                                            {xp_reward_alert}
                                        </Form.Group>
                                    </Card.Body>
                                </Row>
                                <Row>
                                    <Card.Body>
                                        <Form.Label>Review</Form.Label>
                                        <Form.Group className="d-flex flex-column w-50">
                                            <span>
                                                {this.state.review.rating}
                                            </span>
                                            <RangeSlider
                                                value={this.state.review.rating}
                                                min={0}
                                                max={5}
                                                step={0.5}
                                                tooltip="off"
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
                                                rows="5"
                                                onChange={(event) =>
                                                    this.inputUpdateHandler(
                                                        event,
                                                        "note"
                                                    )
                                                }
                                            />
                                        </Form.Group>
                                    </Card.Body>
                                </Row>
                                <Button
                                    variant="primary"
                                    type="submit"
                                    className="mb-5"
                                >
                                    Submit
                                </Button>
                            </Form>
                        </Card>
                    </Container>
                </>
            );
        }
    }
}

export default withRouter(Close);
