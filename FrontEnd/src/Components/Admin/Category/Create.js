import React from "react";
import Form from "react-bootstrap/Form";
import { Col, Button, Row } from "react-bootstrap";
import { Container } from "react-bootstrap";
import ButtonInRow from "../../SmartGadgets/ButtonInRow";
import rules from "../../../Files/validationRules.json";
import {
    formValidation,
    validationFeedback,
    validationClassName,
} from "../../../Validator";
import { withRouter } from "react-router-dom";
import { BASE_API_URL } from "../../../App";

class Create extends React.Component {
    state = {
        category: { name: null },
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

    submitHandler = async (event) => {
        event.preventDefault();
        await this.validateForm();
        if (this.state.form.isValid) {
            fetch(BASE_API_URL + "/category", {
                method: "POST",
                mode: "cors",
                credentials: "include",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(this.state.category),
            }).then((response) => {
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
                    <h1>Vytváranie kategórie</h1>

                    <Form.Group as={Col} controlId="formGridName">
                        <Form.Label>Meno kategórie</Form.Label>
                        <Form.Control
                            className={validationClassName(
                                "name",
                                this.state.form
                            )}
                            placeholder="Enter name"
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

export default withRouter(Create);
