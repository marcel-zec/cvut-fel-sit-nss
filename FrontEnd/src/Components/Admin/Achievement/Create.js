import React from "react";
import Form from "react-bootstrap/Form";
import { Col, Button, Row, OverlayTrigger, Tooltip } from "react-bootstrap";
import { Container } from "react-bootstrap";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import icons from "../../../Files/icons.json";
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

class Create extends React.Component {
    state = {
        achievement: {
            name: null,
            description: null,
            icon: null,
            type: null,
            category: null,
        },
        categories: null,
        form: {
            isValid: false,
            elements: {
                icon: {
                    touched: false,
                    valid: false,
                    validationRules: rules.achievement.icon,
                },
                name: {
                    touched: false,
                    valid: false,
                    validationRules: rules.achievement.name,
                },
                description: {
                    touched: false,
                    valid: false,
                    validationRules: rules.achievement.description,
                },
                type: {
                    touched: false,
                    valid: false,
                    validationRules: {},
                },
                limit: {
                    touched: false,
                    valid: false,
                    validationRules: {},
                },
                category: {
                    touched: false,
                    valid: false,
                    validationRules: {},
                },
            },
        },
        typeButtons: {
            special: {
                selected: true,
                variant: "success",
            },
            categorized: {
                selected: false,
                variant: "outline-primary",
            },
            certificate: {
                selected: false,
                variant: "outline-primary",
            },
        },
    };

    /**
     * Update state from input.
     * @param {event} event
     * @param {String} nameOfFormInput,
     */
    inputUpdateHandler = async (event, nameOfFormInput, input = null) => {
        const newState = { ...this.state.achievement };
        if (input) {
            newState[nameOfFormInput] = input;
        } else {
            if (this.state.form.elements[nameOfFormInput].idForUpdate)
                newState[nameOfFormInput] = event.target.id;
            else newState[nameOfFormInput] = event.target.value;
        }

        await this.setState({ achievement: newState });
        if (this.state.form.elements[nameOfFormInput].touched) {
            this.validateForm();
        }
        console.log(this.state.achievement);
    };

    typeButtonHandler = async (event, input) => {
        this.inputUpdateHandler(event, "type", input);
        if (input === "special") {
            await this.setState({
                categories: null,
                typeButtons: {
                    special: {
                        selected: true,
                        variant: "success",
                    },
                    categorized: {
                        selected: false,
                        variant: "outline-primary",
                    },
                    certificate: {
                        selected: false,
                        variant: "outline-primary",
                    },
                },
            });
        } else if (input === "categorized") {
            this.fetchCategories();
            await this.setState({
                typeButtons: {
                    special: {
                        selected: false,
                        variant: "outline-primary",
                    },
                    categorized: {
                        selected: true,
                        variant: "success",
                    },
                    certificate: {
                        selected: false,
                        variant: "outline-primary",
                    },
                },
            });
        } else if (input === "certificate") {
            await this.setState({
                categories: null,
                typeButtons: {
                    special: {
                        selected: false,
                        variant: "outline-primary",
                    },
                    categorized: {
                        selected: false,
                        variant: "outline-primary",
                    },
                    certificate: {
                        selected: true,
                        variant: "success",
                    },
                },
            });
        }
    };

    fetchCategories = async () => {
        fetch(BASE_API_URL + "/category", {
            method: "GET",
            mode: "cors",
            credentials: "include",
            headers: {
                "Content-Type": "application/json",
            },
        })
            .then((response) => {
                return response.json();
            })
            .then((data) => {
                this.setState({ categories: data });
                console.log(data);
            });
    };

    submitHandler = async (event) => {
        event.preventDefault();
        console.log(this.state.achievement);
        await this.validateForm();
        if (this.state.form.isValid) {
            if (this.state.typeButtons.categorized.selected) {
                let foundIndex = this.state.categories.findIndex(
                    (element) => element.id == this.state.achievement.category
                );
                if (foundIndex > -1) {
                    const newState = { ...this.state };
                    newState.achievement.category = this.state.categories[
                        foundIndex
                    ];
                    await this.setState(newState);
                }
                fetch(BASE_API_URL + "/achievement/categorized", {
                    method: "POST",
                    mode: "cors",
                    credentials: "include",
                    headers: {
                        "Content-Type": "application/json",
                    },
                    body: JSON.stringify(this.state.achievement),
                }).then((response) => {
                    if (response.ok)
                        this.props.history.push({
                            pathname: "/achievement",
                            type: this.state.achievement.type,
                            alert: (
                                <MyAlert
                                    text="Achievement created"
                                    flash={true}
                                />
                            ),
                        });
                    //TODO - osetrenie vynimiek
                    else console.log("Error: somethhing goes wrong");
                });
            } else if (this.state.typeButtons.certificate.selected) {
                fetch(BASE_API_URL + "/achievement/certificate", {
                    method: "POST",
                    mode: "cors",
                    credentials: "include",
                    headers: {
                        "Content-Type": "application/json",
                    },
                    body: JSON.stringify(this.state.achievement),
                }).then((response) => {
                    if (response.ok)
                        this.props.history.push({
                            pathname: "/achievement",
                            type: this.state.achievement.type,
                            alert: (
                                <MyAlert
                                    text="Achievement created"
                                    flash={true}
                                />
                            ),
                        });
                    //TODO - osetrenie vynimiek
                    else console.log("Error: somethhing goes wrong");
                });
            } else {
                fetch(BASE_API_URL + "/achievement/special", {
                    method: "POST",
                    mode: "cors",
                    credentials: "include",
                    headers: {
                        "Content-Type": "application/json",
                    },
                    body: JSON.stringify(this.state.achievement),
                }).then((response) => {
                    if (response.ok)
                        this.props.history.push({
                            pathname: "/achievement",
                            type: this.state.achievement.type,
                            alert: (
                                <MyAlert
                                    text="Achievement created"
                                    flash={true}
                                />
                            ),
                        });
                    //TODO - osetrenie vynimiek
                    else console.log("Error: somethhing goes wrong");
                });
            }
        }
    };

    validateForm = async () => {
        const newState = { ...this.state.form };

        console.log(this.state.typeButtons.categorized.selected);
        if (this.state.typeButtons.categorized.selected) {
            newState.elements.limit.validationRules = rules.achievement.limit;
            newState.elements.category.validationRules =
                rules.achievement.category;
        } else {
            newState.elements.limit.validationRules = {};
            newState.elements.category.validationRules = {};
        }
        formValidation(newState, this.state.achievement);
        await this.setState({ form: newState });
    };

    render() {
        let categorizedForm = null;
        if (this.state.categories) {
            if (this.state.categories.length > 0) {
                let categoryOptions = [];
                let index = 0;
                this.state.categories.forEach((element) => {
                    if (index++ == 0) {
                        categoryOptions.push(
                            <option
                                selected
                                disabled={
                                    this.state.achievement.category
                                        ? true
                                        : false
                                }
                            >
                                Choose..
                            </option>
                        );
                    } else {
                        categoryOptions.push(
                            <option value={element.id}>{element.name}</option>
                        );
                    }
                });
                categorizedForm = (
                    <Form.Row>
                        <Form.Group
                            as={Col}
                            controlId="exampleForm.ControlSelect1"
                        >
                            <Form.Label>Category</Form.Label>
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
                                {categoryOptions}
                            </Form.Control>
                            <span className="invalid-feedback">
                                {validationFeedback(
                                    "category",
                                    this.state.form
                                )}
                            </span>
                        </Form.Group>

                        <Form.Group as={Col} controlId="formGridName">
                            <Form.Label>Level limit</Form.Label>
                            <Form.Control
                                placeholder="Enter limit"
                                onChange={(event) =>
                                    this.inputUpdateHandler(event, "limit")
                                }
                                className={validationClassName(
                                    "limit",
                                    this.state.form
                                )}
                            />
                            <span className="invalid-feedback">
                                {validationFeedback("limit", this.state.form)}
                            </span>
                        </Form.Group>
                    </Form.Row>
                );
            }
        }

        let iconsToForm = [];
        icons.icons.forEach((element) => {
            iconsToForm.push(
                <div className="m-3">
                    <Form.Check.Label>
                        <Form.Check
                            type="radio"
                            name="formHorizontalRadios"
                            value={element.icon}
                            onChange={(event) =>
                                this.inputUpdateHandler(event, "icon")
                            }
                        />
                        <FontAwesomeIcon
                            className={
                                this.state.achievement.icon == element.icon
                                    ? "choosen-icon"
                                    : ""
                            }
                            icon={element.icon}
                            size="3x"
                        />
                    </Form.Check.Label>
                </div>
            );
        });

        return (
            <Container>
                <ButtonInRow
                    variant="danger"
                    link="/achievement"
                    side="left"
                    label=""
                    back={true}
                />

                <Form className="mt-3 mb-5" onSubmit={this.submitHandler}>
                    <h1>Vytváranie achievementu</h1>

                    <Form.Group as={Col} controlId="formGridName">
                        <Form.Label>Meno achievementu</Form.Label>
                        <Form.Control
                            className={validationClassName(
                                "name",
                                this.state.form
                            )}
                            placeholder="Enter name"
                            onChange={(event) =>
                                this.inputUpdateHandler(event, "name")
                            }
                        />
                        <div class="invalid-feedback">
                            {validationFeedback("name", this.state.form)}
                        </div>
                    </Form.Group>

                    <Form.Group controlId="exampleForm.ControlTextarea1">
                        <Form.Label>Popis</Form.Label>
                        <Form.Control
                            as="textarea"
                            rows="5"
                            className={validationClassName(
                                "description",
                                this.state.form
                            )}
                            onChange={(event) =>
                                this.inputUpdateHandler(event, "description")
                            }
                        />
                        <div class="invalid-feedback">
                            {validationFeedback("description", this.state.form)}
                        </div>
                    </Form.Group>

                    <Form.Group
                        className={
                            "d-flex flex-row flex-wrap " +
                            validationClassName("icon", this.state.form)
                        }
                        id="icons"
                    >
                        {iconsToForm}
                    </Form.Group>
                    <div class="invalid-feedback">
                        {validationFeedback("icon", this.state.form)}
                    </div>

                    <Form.Group>
                        <Form.Label as="legend">Typ</Form.Label>
                        <Col>
                            <OverlayTrigger
                                key="special"
                                overlay={
                                    <Tooltip>
                                        Uživateľ získa po absolvovaní tripu.
                                    </Tooltip>
                                }
                            >
                                <Button
                                    variant={
                                        this.state.typeButtons.special.variant
                                    }
                                    onClick={(event) =>
                                        this.typeButtonHandler(event, "special")
                                    }
                                    className="m-3"
                                >
                                    Brigádnický
                                </Button>
                            </OverlayTrigger>

                            <OverlayTrigger
                                key="categorized"
                                overlay={
                                    <Tooltip>
                                        Achievement získa uživateľ za zadaný
                                        počet absolvovaných brigád v tejto
                                        kategórií.
                                    </Tooltip>
                                }
                            >
                                <Button
                                    variant={
                                        this.state.typeButtons.categorized
                                            .variant
                                    }
                                    onClick={(event) =>
                                        this.typeButtonHandler(
                                            event,
                                            "categorized"
                                        )
                                    }
                                    className="m-3"
                                >
                                    Kategorizovaný
                                </Button>
                            </OverlayTrigger>

                            <OverlayTrigger
                                key="certificate"
                                overlay={
                                    <Tooltip>
                                        Uživateľ získa po nahraní certifikátu a
                                        overení administrátorom.
                                    </Tooltip>
                                }
                            >
                                <Button
                                    variant={
                                        this.state.typeButtons.certificate
                                            .variant
                                    }
                                    onClick={(event) =>
                                        this.typeButtonHandler(
                                            event,
                                            "certificate"
                                        )
                                    }
                                    className="m-3"
                                >
                                    Certifikát
                                </Button>
                            </OverlayTrigger>
                        </Col>
                    </Form.Group>
                    <div class="invalid-feedback">
                        {validationFeedback("type", this.state.form)}
                    </div>

                    {categorizedForm}

                    <Button variant="primary" type="submit">
                        Submit
                    </Button>
                </Form>
            </Container>
        );
    }
}

export default withRouter(Create);
