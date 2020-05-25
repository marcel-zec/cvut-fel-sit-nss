import React from "react";
import Form from "react-bootstrap/Form";
import { Col, Button, Spinner } from "react-bootstrap";
import { Container } from "react-bootstrap";
import ButtonInRow from "../../SmartGadgets/ButtonInRow";
import rules from "../../../Files/validationRules.json";
import { withRouter } from "react-router-dom";
import {
    formValidation,
    validationFeedback,
    validationClassName,
} from "../../../Validator";
import { BASE_API_URL } from "../../../App";

class Edit extends React.Component {
    state = {
        category: null,
        form: {
            isValid: false,
            elements: {
                name: {
                    touched: false,
                    valid: false,
                    validationRules: rules.category.name,
                },
            },
        },
    };

    async componentDidMount() {
        const response = await fetch(
            BASE_API_URL + "/category/" + this.props.match.params.id,
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
        this.setState({ category: data });
    }

    submitHandler = async (event) => {
        event.preventDefault();
        console.log(this.state.category);
        await this.validateForm();
        if (this.state.form.isValid) {
            fetch(
                "http://localhost:8080/category/" + this.props.match.params.id,
                {
                    method: "PATCH",
                    mode: "cors",
                    credentials: "include",
                    headers: {
                        "Content-Type": "application/json",
                    },
                    body: JSON.stringify(this.state.category),
                }
            ).then((response) => {
                if (response.ok) this.props.history.push("/category");
                //TODO - osetrenie vynimiek
                else console.log("Error: somethhing goes wrong");
            });
        }
    };

    /**
     * Update state from input.
     * @param {event} event
     * @param {String} nameOfFormInput,
     */
    inputUpdateHandler = async (event, nameOfFormInput, number = false) => {
        const newState = { ...this.state.category };
        let input = event.target.value;
        if (number) {
            input = event.target.value.replace(/,/g, ".");
        }
        newState[nameOfFormInput] = input;
        await this.setState({ category: newState });
        if (this.state.form.elements[nameOfFormInput].touched) {
            this.validateForm();
        }
    };

    validateForm = async () => {
        const newState = { ...this.state.form };
        formValidation(newState, this.state.category);
        await this.setState({ form: newState });
    };

    render() {
        if (this.state.category === null) {
            return (
                <Container className="mt-5 p-5">
                    <Spinner animation="border" role="status">
                        <span className="sr-only">Loading...</span>
                    </Spinner>
                </Container>
            );
        } else {
            return (
                <Container>
                    <ButtonInRow
                        variant="danger"
                        link="/category"
                        side="left"
                        label=""
                        back={true}
                    />

                    <Form className="mt-3 mb-5" onSubmit={this.submitHandler}>
                        <h1>Edit category</h1>

                        <Form.Group as={Col} controlId="formGridName">
                            <Form.Label>Name of category</Form.Label>
                            <Form.Control
                                className={validationClassName(
                                    "name",
                                    this.state.form
                                )}
                                placeholder="Enter name"
                                value={this.state.category.name}
                                onChange={(event) =>
                                    this.inputUpdateHandler(event, "name", true)
                                }
                            />
                            <div class="invalid-feedback">
                                {validationFeedback("name", this.state.form)}
                            </div>
                        </Form.Group>

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
