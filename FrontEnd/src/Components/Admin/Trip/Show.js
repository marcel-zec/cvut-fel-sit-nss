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
                deposit: {
                    touched: false,
                    valid: false,
                    validationRules: rules.trip.deposit,
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
            deposit: null,
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
                    <Form.Control as="select" disabled>
                        {categoriesArray}
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

                    <Form className="mt-3 mb-5">
                        <h1>Show job</h1>
                        <Form.Row>
                            <Form.Group as={Col} controlId="formGridName">
                                <Form.Label>Name of job</Form.Label>
                                <Form.Control
                                    placeholder="Enter name"
                                    value={this.state.trip.name}
                                    disabled
                                />
                            </Form.Group>

                            <Form.Group as={Col} controlId="formGridShortName">
                                <Form.Label>Identificatation name</Form.Label>
                                <Form.Control
                                    placeholder="Enter unique key for trip"
                                    value={this.state.trip.short_name}
                                    disabled
                                />
                            </Form.Group>
                        </Form.Row>
                        <Form.Row>
                            <Form.Group as={Col} controlId="formGridDeposit">
                                <Form.Label>Salary (per hour)</Form.Label>
                                <Form.Control
                                    placeholder="Enter salary (per hour)"
                                    value={this.state.trip.deposit}
                                    disabled
                                />
                            </Form.Group>

                            <Form.Group as={Col} controlId="formGridExperience">
                                <Form.Label>Required level</Form.Label>
                                <Form.Control
                                    placeholder="Enter minimum reqiured level"
                                    value={this.state.trip.required_level}
                                    disabled
                                />
                            </Form.Group>
                        </Form.Row>
                        <Form.Row>
                            <Form.Group
                                as={Col}
                                controlId="exampleForm.ControlSelect1"
                            >
                                <Form.Label>Possible XP reward</Form.Label>
                                <Form.Control as="select" disabled>
                                    {possibleXPrewardOptions}
                                </Form.Control>
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
                                disabled
                            />
                        </Form.Group>
                        <Form.Group controlId="exampleForm.ControlTextarea1">
                            <Form.Label>Description</Form.Label>
                            <Form.Control
                                as="textarea"
                                rows="5"
                                value={this.state.trip.description}
                                disabled
                            />
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
                            uneditable={true}
                        />
                        <Form.Group>
                            <SessionGroup
                                onChangeMethod={this.inputSessionUpdateHandler}
                                sessions={this.state.trip.sessions}
                                forDeleteSession={this.sessionDeleteHandler}
                                uneditable={true}
                            />
                        </Form.Group>
                        <div class="invalid-feedback">
                            {this.state.form.elements.sessions.feedback}
                        </div>
                    </Form>
                </Container>
            );
        }
    }
}

export default withRouter(Edit);
