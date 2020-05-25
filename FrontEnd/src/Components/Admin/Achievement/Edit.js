import React from "react";
import Form from "react-bootstrap/Form";
import { Col, Button, Row } from "react-bootstrap";
import { Container } from "react-bootstrap";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import icons from "../../../Files/icons.json";
import Spinner from "react-bootstrap/Spinner";
import ButtonInRow from "../../SmartGadgets/ButtonInRow";
import rules from "../../../Files/validationRules.json";
import { withRouter } from "react-router-dom";
import {
    formValidation,
    validationFeedback,
    validationClassName,
} from "../../../Validator";
import MyAlert from "../../SmartGadgets/MyAlert";

class Edit extends React.Component {
    state = {
        type: null,
        achievement: {
            name: null,
            description: null,
            icon: null,
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
    };

    async componentDidMount() {
        const type = this.props.location.type;
        const requestSettings = {
            method: "GET",
            mode: "cors",
            credentials: "include",
            headers: {
                "Content-Type": "application/json",
            },
        };
        if (type == "categorized") {
            const response = await fetch(
                `http://localhost:8080/achievement/categorized/` +
                    this.props.match.params.id,
                requestSettings
            );
            const data = await response.json();
            const dataCopy = { ...data };
            dataCopy.category = data.category.id;
            this.setState({ achievement: dataCopy });

            const response1 = await fetch(
                `http://localhost:8080/category`,
                requestSettings
            );
            const data1 = await response1.json();
            this.setState({ categories: data1 });
        } else if (type == "certificate") {
            const response = await fetch(
                `http://localhost:8080/achievement/certificate/` +
                    this.props.match.params.id,
                requestSettings
            );
            const data = await response.json();
            this.setState({ achievement: data });
        } else if (type == "special") {
            const response = await fetch(
                `http://localhost:8080/achievement/special/` +
                    this.props.match.params.id,
                requestSettings
            );
            const data = await response.json();
            this.setState({ achievement: data });
        }
        /*const response = await fetch(
            `http://localhost:8080/achievement/` + this.props.match.params.id
        );
        const data = await response.json();
        console.log(data);
        this.setState({ achievement: data });*/
        console.log(this.state.achievement);
        console.log(this.state.categories);
        await this.setState({ type: type });
    }

    /**
     * Update state from input.
     * @param {event} event
     * @param {String} nameOfFormInput,
     */
    inputUpdateHandler = async (event, nameOfFormInput) => {
        const newState = { ...this.state.achievement };
        newState[nameOfFormInput] = event.target.value;
        await this.setState({ achievement: newState });
        if (this.state.form.elements[nameOfFormInput].touched) {
            this.validateForm();
        }
    };

    submitHandler = async (event) => {
        event.preventDefault();
        console.log(this.state);
        await this.validateForm();
        if (this.state.form.isValid) {
            if (this.state.type == "categorized") {
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
                this.fetchToBackend("categorized");
            } else if (this.state.type == "certificate") {
                this.fetchToBackend("certificate");
            } else if (this.state.type == "special") {
                this.fetchToBackend("special");
            }
        }
    };

    fetchToBackend = (type) => {
        fetch(
            "http://localhost:8080/achievement/" +
                type +
                "/" +
                this.props.match.params.id,
            {
                method: "PATCH",
                credentials: "include",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(this.state.achievement),
            }
        ).then((response) => {
            if (response.ok)
                this.props.history.push({
                    pathname: "/achievement",
                    type: type,
                    alert: <MyAlert text="Achievement updated" flash={true} />,
                });
        });
    };

    validateForm = async () => {
        const newState = { ...this.state.form };
        if (this.state.type == "categorized") {
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
        if (this.state.achievement === null) {
            return (
                <Container className="mt-5 p-5">
                    <Spinner animation="border" role="status">
                        <span className="sr-only">Loading...</span>
                    </Spinner>
                </Container>
            );
        } else {
            let categorizedForm = null;
            if (this.state.type == "categorized") {
                let categoryOptions = [];
                if (this.state.categories && this.state.categories.length > 0) {
                    this.state.categories.forEach((element) => {
                        if (element.id == this.state.achievement.category) {
                            categoryOptions.push(
                                <option value={element.id} selected>
                                    {element.name}
                                </option>
                            );
                        } else {
                            categoryOptions.push(
                                <option value={element.id}>
                                    {element.name}
                                </option>
                            );
                        }
                    });
                }
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
                                value={this.state.achievement.limit}
                            />
                            <span className="invalid-feedback">
                                {validationFeedback("limit", this.state.form)}
                            </span>
                        </Form.Group>
                    </Form.Row>
                );
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
                                checked={
                                    this.state.achievement.icon == element.icon
                                        ? "checked"
                                        : ""
                                }
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
                        link={{
                            pathname: "/achievement",
                            type: this.state.type,
                        }}
                        side="left"
                        label=""
                        back={true}
                    />

                    <Form className="mt-3 mb-5" onSubmit={this.submitHandler}>
                        <h1>Edit achievement</h1>

                        <Form.Group as={Col} controlId="formGridName">
                            <Form.Label>Name of trip</Form.Label>
                            <Form.Control
                                value={this.state.achievement.name}
                                onChange={(event) =>
                                    this.inputUpdateHandler(event, "name")
                                }
                                className={validationClassName(
                                    "name",
                                    this.state.form
                                )}
                            />
                            <div class="invalid-feedback">
                                {validationFeedback("name", this.state.form)}
                            </div>
                        </Form.Group>

                        <Form.Group controlId="exampleForm.ControlTextarea1">
                            <Form.Label>Description</Form.Label>
                            <Form.Control
                                as="textarea"
                                rows="5"
                                value={this.state.achievement.description}
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

                        <Form.Group
                            className={
                                "d-flex flex-row flex-wrap " +
                                validationClassName("icon", this.state.form)
                            }
                        >
                            {iconsToForm}
                        </Form.Group>
                        <div class="invalid-feedback">
                            {validationFeedback("icon", this.state.form)}
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
}

export default withRouter(Edit);
