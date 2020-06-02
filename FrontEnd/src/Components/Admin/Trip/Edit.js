import React from "react";
import Form from "react-bootstrap/Form";
import { Col, Button, Row, Spinner } from "react-bootstrap";
import { Container } from "react-bootstrap";
import Achievements from "./UI/Achievements";
import SessionGroup from "./SessionGroup";
import ButtonInRow from "../../SmartGadgets/ButtonInRow";
import rules from "../../../Files/validationRules.json";
import { withRouter } from "react-router-dom";
import {
    formValidation,
    validationFeedback,
    validationClassName,
} from "../../../Validator";
import MyAlert from "../../SmartGadgets/MyAlert";
import { BASE_API_URL } from "../../../App";

class Edit extends React.Component {
    state = {
        achievements_special: null,
        achievements_categorized: null,
        achievements_certificate: null,
        categories: null,
        form: {
            isValid: false,
            elements: {
                name: {
                    touched: false,
                    valid: false,
                    validationRules: rules.trip.name,
                },
                short_name: {
                    touched: false,
                    valid: false,
                    validationRules: rules.trip.short_name,
                },
                salary: {
                    touched: false,
                    valid: false,
                    validationRules: rules.trip.salary,
                },
                required_level: {
                    touched: false,
                    valid: false,
                    validationRules: rules.trip.required_level,
                },
                possible_xp_reward: {
                    touched: false,
                    valid: false,
                    validationRules: rules.trip.possible_xp_reward,
                },
                category: {
                    touched: false,
                    valid: false,
                    validationRules: rules.trip.category,
                },
                location: {
                    touched: false,
                    valid: false,
                    validationRules: rules.trip.location,
                },
                description: {
                    touched: false,
                    valid: false,
                    validationRules: rules.trip.description,
                },
                sessions: {
                    touched: false,
                    valid: false,
                    feedback: null,
                },
            },
        },
        trip: {
            name: null,
            short_name: null,
            salary: null,
            required_level: null,
            possible_xp_reward: null,
            category: null,
            location: null,
            description: null,
            required_achievements_special: [],
            required_achievements_certificate: [],
            required_achievements_categorized: [],
            gain_achievements_special: [],
            sessions: [],
        },
    };

    /**
     * Update state from input.
     * @param {event} event
     * @param {String} nameOfFormInput
     * @param {Boolean} arrayToPush - if want push to array
     * @param {Boolean} checkbox
     */
    inputUpdateHandler = async (event, nameOfFormInput) => {
        const stringProperties = [
            "name",
            "short_name",
            "salary",
            "required_level",
            "possible_xp_reward",
            "location",
            "description",
        ];
        const checkboxProperties = [
            "required_achievements_special",
            "required_achievements_certificate",
            "required_achievements_categorized",
            "gain_achievements_special",
        ];
        const newState = { ...this.state.trip };

        //string inputs
        if (stringProperties.includes(nameOfFormInput)) {
            newState[nameOfFormInput] = event.target.value;
        } else if (checkboxProperties.includes(nameOfFormInput)) {
            //if already checked
            let foundIndex = newState[nameOfFormInput].findIndex((object) => {
                return object.id == event.target.value;
            });
            //remove if alredy checked
            if (foundIndex > -1) {
                newState[nameOfFormInput].splice(foundIndex, 1);
                console.log("state after splice");
                console.log(newState[nameOfFormInput]);
            } else {
                let objectIndex = -1;
                if (nameOfFormInput == "required_achievements_special") {
                    objectIndex = this.state.achievements_special.findIndex(
                        (object) => object.id == event.target.value
                    );
                    if (objectIndex > -1) {
                        newState[nameOfFormInput].push(
                            this.state.achievements_special[objectIndex]
                        );
                    }
                } else if (
                    nameOfFormInput == "required_achievements_certificate"
                ) {
                    objectIndex = this.state.achievements_certificate.findIndex(
                        (object) => object.id == event.target.value
                    );
                    if (objectIndex > -1) {
                        newState[nameOfFormInput].push(
                            this.state.achievements_certificate[objectIndex]
                        );
                    }
                } else if (
                    nameOfFormInput == "required_achievements_categorized"
                ) {
                    objectIndex = this.state.achievements_categorized.findIndex(
                        (object) => object.id == event.target.value
                    );
                    if (objectIndex > -1) {
                        newState[nameOfFormInput].push(
                            this.state.achievements_categorized[objectIndex]
                        );
                    }
                } else if (nameOfFormInput == "gain_achievements_special") {
                    objectIndex = this.state.achievements_special.findIndex(
                        (object) => object.id == event.target.value
                    );
                    if (objectIndex > -1) {
                        newState[nameOfFormInput].push(
                            this.state.achievements_special[objectIndex]
                        );
                    }
                }
            }
        } else if (nameOfFormInput == "category") {
            console.log(event.target.value);
            let foundIndex = this.state.categories.findIndex(
                (category) => category.name == event.target.value
            );
            if (foundIndex > -1) {
                newState.category = this.state.categories[foundIndex];
            }
        }
        await this.setState({ trip: newState });
        if (
            this.state.form.elements.hasOwnProperty(nameOfFormInput) &&
            this.state.form.elements[nameOfFormInput].touched
        ) {
            this.validateForm();
        }
    };

    sessionDeleteHandler = async (session) => {
        let newState = [...this.state.trip.sessions];
        const found = newState.findIndex((element) => {
            return element.index == session.index;
        });
        if (found > -1) {
            newState.splice(found, 1);
        }
        await this.setState((oldState) => ({
            trip: {
                ...oldState.trip,
                sessions: newState,
            },
        }));
        console.log(this.state.trip.sessions);
    };

    inputSessionUpdateHandler = async (session) => {
        console.log("input session update, trip before update");
        console.log(this.state.trip);
        let newState = { ...this.state };
        const found = newState.trip.sessions.findIndex((element) => {
            return element.index == session.index;
        });
        console.log("found: " + found);
        if (found > -1) {
            console.log("if in inputSessionUpdate");
            newState.trip.sessions[found] = session;
        } else {
            console.log("else in inputSessionUpdate");
            session.index = this.state.trip.sessions.length;
            newState.trip.sessions.push(session);
            console.log("new");
            console.log(newState);
        }
        await this.setState(newState);
        console.log("input session update, trip after update");
        console.log(this.state.trip);
    };

    validateForm = async () => {
        const newState = { ...this.state.form };
        formValidation(newState, this.state.trip);
        newState.elements.sessions.feedback = null;
        await this.setState({ form: newState });
        let sessionValid = true;
        newState.elements.sessions.touched = true;
        if (this.state.trip.sessions.length > 0) {
            let index = 1;
            this.state.trip.sessions.forEach((session) => {
                console.log(session.capacity);
                if (session.from_date == null) {
                    if (newState.elements.sessions.feedback)
                        newState.elements.sessions.feedback +=
                            "Missing 'from' date at " + index + ". session. ";
                    else
                        newState.elements.sessions.feedback =
                            "Missing 'from' date at " + index + ". session. ";
                    sessionValid = false;
                }
                if (session.to_date == null) {
                    if (newState.elements.sessions.feedback)
                        newState.elements.sessions.feedback +=
                            "Missing 'to' date at " + index + ". session. ";
                    else
                        newState.elements.sessions.feedback =
                            "Missing 'to' date at " + index + ". session. ";
                    sessionValid = false;
                }
                if (session.capacity == null) {
                    if (newState.elements.sessions.feedback)
                        newState.elements.sessions.feedback +=
                            "Missing 'capacity' at " + index + ". session.";
                    else
                        newState.elements.sessions.feedback =
                            "Missing 'capacity' date at " +
                            index +
                            ". session.";
                    sessionValid = false;
                } else if (isNaN(parseInt(session.capacity))) {
                    if (newState.elements.sessions.feedback)
                        newState.elements.sessions.feedback +=
                            "Capacity at " +
                            index +
                            ". session needs to be number. ";
                    else
                        newState.elements.sessions.feedback =
                            "Capacity at " +
                            index +
                            ". session needs to be number. ";
                    sessionValid = false;
                } else if (session.capacity < 0 || session.capacity > 99999) {
                    if (newState.elements.sessions.feedback)
                        newState.elements.sessions.feedback +=
                            "Capacity at " +
                            index +
                            ". session needs to be at range 0 - 99 999. ";
                    else
                        newState.elements.sessions.feedback =
                            "Capacity at " +
                            index +
                            ". session needs to be at range 0 - 99 999. ";
                }
                index++;
            });
        } else {
            if (newState.elements.sessions.feedback)
                newState.elements.sessions.feedback +=
                    "Trip needs to have at least one session. ";
            else
                newState.elements.sessions.feedback =
                    "Trip needs to have at least one session. ";
            sessionValid = false;
        }
        newState.elements.sessions.valid = sessionValid;
        await this.setState({ form: newState });
    };

    submitHandler = async (event) => {
        event.preventDefault();
        await this.validateForm();
        if (this.state.form.isValid) {
            console.log("before patch");
            console.log(this.state.trip);
            fetch(BASE_API_URL + "/trip/" + this.props.match.params.id, {
                method: "PATCH",
                credentials: "include",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(this.state.trip),
            })
                .then((response) => {
                    if (response.ok) {
                        this.props.history.push({
                            pathname: "/trip",
                            alert: <MyAlert text="Trip updated" flash={true} />,
                        });
                    } else console.error(response.status);
                })
                .catch((error) => {
                    console.error(error);
                });
        }
    };

    async componentDidMount() {
        const requestSettings = {
            method: "GET",
            credentials: "include",
            headers: {
                "Content-Type": "application/json",
            },
        };
        await fetch(
            BASE_API_URL + "/trip/" + this.props.match.params.id,
            requestSettings
        )
            .then((response) => {
                if (response.ok) return response.json();
                else console.error(response.status);
            })
            .then((data) => {
                this.setState({ trip: data });
            })
            .catch((error) => {
                console.error(error);
            });

        await fetch(BASE_API_URL + "/category", requestSettings)
            .then((response) => {
                if (response.ok) return response.json();
                else console.error(response.status);
            })
            .then((data) => {
                this.setState({ categories: data });
            })
            .catch((error) => {
                console.error(error);
            });

        await fetch(BASE_API_URL + "/achievement/categorized", requestSettings)
            .then((response) => {
                if (response.ok) return response.json();
                else console.error(response.status);
            })
            .then((data) => {
                this.setState({ achievements_categorized: data });
            })
            .catch((error) => {
                console.error(error);
            });

        await fetch(BASE_API_URL + "/achievement/special", requestSettings)
            .then((response) => {
                if (response.ok) return response.json();
                else console.error(response.status);
            })
            .then((data) => {
                this.setState({ achievements_special: data });
            })
            .catch((error) => {
                console.error(error);
            });

        await fetch(BASE_API_URL + "/achievement/certificate", requestSettings)
            .then((response) => {
                if (response.ok) return response.json();
                else console.error(response.status);
            })
            .then((data) => {
                this.setState({ achievements_certificate: data });
            })
            .catch((error) => {
                console.error(error);
            });
    }

    render() {
        if (
            this.state.achievements_required == null &&
            this.state.achievements_gain == null &&
            this.state.categories == null
        ) {
            return (
                <Container className="p-5 mt-5">
                    <Spinner animation="border" role="status">
                        <span className="sr-only">Loading...</span>
                    </Spinner>
                </Container>
            );
        } else {
            let possibleXPrewardOptions = [];
            for (let i = 0; i < 25; i++) {
                if (this.state.trip.possible_xp_reward == i + 1) {
                    possibleXPrewardOptions.push(
                        <option selected>{i + 1}</option>
                    );
                } else {
                    possibleXPrewardOptions.push(<option>{i + 1}</option>);
                }
            }

            let categoryOptions = null;
            if (this.state.categories.length > 0) {
                let categoriesArray = [];
                categoriesArray.push(<option>Select category..</option>);

                this.state.categories.forEach((element) => {
                    if (
                        this.state.trip.category &&
                        this.state.trip.category.name == element.name
                    ) {
                        categoriesArray.push(
                            <option selected>{element.name}</option>
                        );
                    } else {
                        categoriesArray.push(<option>{element.name}</option>);
                    }
                });
                categoryOptions = (
                    <Form.Control
                        as="select"
                        onChange={(event) =>
                            this.inputUpdateHandler(event, "category")
                        }
                        className={validationClassName(
                            "category",
                            this.state.form
                        )}
                    >
                        {categoriesArray}
                        <div class="invalid-feedback">
                            {validationFeedback("category", this.state.form)}
                        </div>
                    </Form.Control>
                );
            }

            return (
                <Container>
                    <ButtonInRow
                        variant="danger"
                        link="/trip"
                        side="left"
                        label=""
                        back={true}
                    />

                    <Form className="mt-3 mb-5" onSubmit={this.submitHandler}>
                        <h1>Edit job</h1>
                        <Form.Row>
                            <Form.Group as={Col} controlId="formGridName">
                                <Form.Label>Name of job</Form.Label>
                                <Form.Control
                                    placeholder="Enter name"
                                    value={this.state.trip.name}
                                    onChange={(event) =>
                                        this.inputUpdateHandler(event, "name")
                                    }
                                    className={validationClassName(
                                        "name",
                                        this.state.form
                                    )}
                                />
                                <div class="invalid-feedback">
                                    {validationFeedback(
                                        "name",
                                        this.state.form
                                    )}
                                </div>
                            </Form.Group>

                            <Form.Group as={Col} controlId="formGridShortName">
                                <Form.Label>Identificatation name</Form.Label>
                                <Form.Control
                                    placeholder="Enter unique key for trip"
                                    value={this.state.trip.short_name}
                                    onChange={(event) =>
                                        this.inputUpdateHandler(
                                            event,
                                            "short_name"
                                        )
                                    }
                                    className={validationClassName(
                                        "short_name",
                                        this.state.form
                                    )}
                                />
                                <div class="invalid-feedback">
                                    {validationFeedback(
                                        "short_name",
                                        this.state.form
                                    )}
                                </div>
                            </Form.Group>
                        </Form.Row>
                        <Form.Row>
                            <Form.Group as={Col} controlId="formGridsalary">
                                <Form.Label>Salary (per hour)</Form.Label>
                                <Form.Control
                                    placeholder="Enter salary (per hour)"
                                    value={this.state.trip.salary}
                                    onChange={(event) =>
                                        this.inputUpdateHandler(event, "salary")
                                    }
                                    className={validationClassName(
                                        "salary",
                                        this.state.form
                                    )}
                                />
                                <div class="invalid-feedback">
                                    {validationFeedback(
                                        "salary",
                                        this.state.form
                                    )}
                                </div>
                            </Form.Group>

                            <Form.Group as={Col} controlId="formGridExperience">
                                <Form.Label>Required level</Form.Label>
                                <Form.Control
                                    placeholder="Enter minimum reqiured level"
                                    value={this.state.trip.required_level}
                                    onChange={(event) =>
                                        this.inputUpdateHandler(
                                            event,
                                            "required_level"
                                        )
                                    }
                                    className={validationClassName(
                                        "required_level",
                                        this.state.form
                                    )}
                                />
                                <div class="invalid-feedback">
                                    {validationFeedback(
                                        "required_level",
                                        this.state.form
                                    )}
                                </div>
                            </Form.Group>
                        </Form.Row>
                        <Form.Row>
                            <Form.Group
                                as={Col}
                                controlId="exampleForm.ControlSelect1"
                            >
                                <Form.Label>Possible XP reward</Form.Label>
                                <Form.Control
                                    as="select"
                                    onChange={(event) =>
                                        this.inputUpdateHandler(
                                            event,
                                            "possible_xp_reward"
                                        )
                                    }
                                    className={validationClassName(
                                        "possible_xp_reward",
                                        this.state.form
                                    )}
                                >
                                    {possibleXPrewardOptions}
                                </Form.Control>
                                <div class="invalid-feedback">
                                    {validationFeedback(
                                        "possible_xp_reward",
                                        this.state.form
                                    )}
                                </div>
                            </Form.Group>

                            <Form.Group
                                as={Col}
                                controlId="exampleForm.ControlSelect1"
                            >
                                <Form.Label>Category</Form.Label>
                                {categoryOptions}
                            </Form.Group>
                        </Form.Row>
                        <Form.Group controlId="formGridLocation">
                            <Form.Label>Location</Form.Label>
                            <Form.Control
                                placeholder="Enter address"
                                value={this.state.trip.location}
                                onChange={(event) =>
                                    this.inputUpdateHandler(event, "location")
                                }
                                className={validationClassName(
                                    "location",
                                    this.state.form
                                )}
                            />
                            <div class="invalid-feedback">
                                {validationFeedback(
                                    "location",
                                    this.state.form
                                )}
                            </div>
                        </Form.Group>
                        <Form.Group controlId="exampleForm.ControlTextarea1">
                            <Form.Label>Description</Form.Label>
                            <Form.Control
                                as="textarea"
                                rows="5"
                                value={this.state.trip.description}
                                onChange={(event) =>
                                    this.inputUpdateHandler(
                                        event,
                                        "description"
                                    )
                                }
                                className={validationClassName(
                                    "description",
                                    this.state.form
                                )}
                            />
                            <div class="invalid-feedback">
                                {validationFeedback(
                                    "description",
                                    this.state.form
                                )}
                            </div>
                        </Form.Group>

                        <Achievements
                            itemsGain={this.state.achievements_special}
                            itemsRequired={{
                                special: this.state.achievements_special,
                                categorized: this.state
                                    .achievements_categorized,
                                certificate: this.state
                                    .achievements_certificate,
                            }}
                            selectedGain={
                                this.state.trip.gain_achievements_special
                            }
                            selectedRequired={{
                                special: this.state.trip
                                    .required_achievements_special,
                                categorized: this.state.trip
                                    .required_achievements_categorized,
                                certificate: this.state.trip
                                    .required_achievements_certificate,
                            }}
                            onChangeMethod={this.inputUpdateHandler}
                        />
                        <Form.Group
                            className={validationClassName(
                                "sessions",
                                this.state.form
                            )}
                        >
                            <SessionGroup
                                onChangeMethod={this.inputSessionUpdateHandler}
                                sessions={this.state.trip.sessions}
                                forDeleteSession={this.sessionDeleteHandler}
                            />
                        </Form.Group>
                        <div class="invalid-feedback">
                            {this.state.form.elements.sessions.feedback}
                        </div>
                        <Button variant="primary" type="submit">
                            Submit
                        </Button>
                    </Form>
                </Container>
            );
        }
    }
}

export default withRouter(Edit);
