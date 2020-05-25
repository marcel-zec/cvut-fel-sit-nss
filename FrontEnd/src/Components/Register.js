import React from "react";
import Form from "react-bootstrap/Form";
import { Col, Button, Row, Spinner } from "react-bootstrap";
import { Container } from "react-bootstrap";
import image from "../Files/images/register.jpg";
import rules from "../Files/validationRules.json";
import DICTIONARY from "../Files/dictionary.json";
import {
    formValidation,
    validationFeedback,
    validationClassName,
} from "../Validator";
import { withRouter } from "react-router-dom";
import MyAlert from "./SmartGadgets/MyAlert";

class Register extends React.Component {
    state = {
        form: {
            isValid: false,
            elements: {
                firstName: {
                    touched: false,
                    valid: false,
                    validationRules: rules.registration.firstName,
                },
                lastName: {
                    touched: false,
                    valid: false,
                    validationRules: rules.registration.lastName,
                },
                email: {
                    touched: false,
                    valid: false,
                    validationRules: rules.registration.email,
                },
                password: {
                    touched: false,
                    valid: false,
                    validationRules: rules.registration.password,
                },
                password_control: {
                    touched: false,
                    valid: false,
                    validationRules: rules.registration.password_control,
                },
                city: {
                    touched: false,
                    valid: false,
                    validationRules: rules.address.city,
                },
                street: {
                    touched: false,
                    valid: false,
                    validationRules: rules.address.street,
                },
                houseNumber: {
                    touched: false,
                    valid: false,
                    validationRules: rules.address.houseNumber,
                },
                zipCode: {
                    touched: false,
                    valid: false,
                    validationRules: rules.address.zipCode,
                },
                country: {
                    touched: false,
                    valid: false,
                    validationRules: rules.address.country,
                },
            },
        },
        user: {
            firstName: null,
            lastName: null,
            email: null,
            password: null,
            city: null,
            street: null,
            houseNumber: null,
            zipCode: null,
            country: null,
            password_control: null,
        },
    };

    inputUpdateHandler = async (event, nameOfFormInput, select) => {
        let newStateUser = { ...this.state.user };
        let newStateAddress = { ...this.state.address };

        if (Object.keys(newStateAddress).includes(nameOfFormInput)) {
            newStateAddress[nameOfFormInput] = event.target.value;
            await this.setState({ address: newStateAddress });
        } else if (Object.keys(newStateUser).includes(nameOfFormInput)) {
            if (select && event.target.value == 0) {
                newStateUser[nameOfFormInput] = null;
            } else {
                newStateUser[nameOfFormInput] = event.target.value;
            }
            await this.setState({ user: newStateUser });
        } else if (nameOfFormInput == "password_control") {
            await this.setState({ password_control: event.target.value });
        }

        if (this.state.form.elements[nameOfFormInput].touched) {
            this.validateForm();
        }
        console.log(this.state);
    };

    validateForm = async () => {
        let newState = { ...this.state.form };
        formValidation(newState, this.state.user);
        await this.setState({ form: newState });
        /* newState = { ...this.state.form };
        formValidation(newState, this.state.address);
        await this.setState({ form: newState });*/
    };

    submitHandler = async (event) => {
        event.preventDefault();
        console.log(this.state.user);
        this.validateForm();
        let user = {
            firstName: this.state.user.firstName,
            lastName: this.state.user.lastName,
            email: this.state.user.email,
            password: this.state.user.password,
            address: {
                city: this.state.user.city,
                street: this.state.user.street,
                houseNumber: this.state.user.houseNumber,
                zipCode: this.state.user.zipCode,
                country: this.state.user.country,
            },
        };
        let objectToSend = {
            user: user,
            password_control: this.state.user.password_control,
        };
        console.log(objectToSend);
        fetch("http://localhost:8080/user", {
            method: "POST",
            mode: "cors",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(objectToSend),
        }).then((response) => {
            if (response.ok)
                this.props.history.push({
                    pathname: "/login",
                    alert: (
                        <MyAlert
                            heading="Successful registration"
                            text="Continue by logging in."
                            flash={true}
                        />
                    ),
                });
            //TODO - osetrenie vynimiek
            else console.log("Error: somethhing goes wrong");
        });
    };
    render() {
        let countryOptions = null;
        if (DICTIONARY.countries.length > 0) {
            let countriesArray = [];

            countriesArray.push(
                <option value="0">{DICTIONARY.form.first_option}</option>
            );

            for (var i = 0; i < DICTIONARY.countries.length; i++) {
                countriesArray.push(
                    <option key={i + 1} value={DICTIONARY.countries[i]}>
                        {DICTIONARY.countries[i]}
                    </option>
                );
            }
            countryOptions = (
                <>
                    <Form.Control
                        as="select"
                        placeholder="Enter name"
                        onChange={(event) =>
                            this.inputUpdateHandler(event, "country", true)
                        }
                        className={validationClassName(
                            "country",
                            this.state.form
                        )}
                    >
                        {countriesArray}
                    </Form.Control>
                    <div class="invalid-feedback">
                        {validationFeedback("country", this.state.form)}
                    </div>
                </>
            );
        }
        return (
            <Container className="register_container">
                <Row>
                    <Col xs={5}>
                        <img src={image} />
                    </Col>
                    <Col>
                        <h3>Register new user</h3>
                        <Form
                            className="registerUserForm mt-3 mb-5"
                            onSubmit={this.submitHandler}
                        >
                            <div className="window radius">
                                <h5>Account information</h5>
                                <Form.Row>
                                    <Form.Group as={Col}>
                                        <Form.Label>First name</Form.Label>
                                        <Form.Control
                                            maxLength="30"
                                            autoComplete="name"
                                            placeholder="Enter your first name"
                                            onChange={(event) =>
                                                this.inputUpdateHandler(
                                                    event,
                                                    "firstName"
                                                )
                                            }
                                            className={validationClassName(
                                                "firstName",
                                                this.state.form
                                            )}
                                        />
                                        <div class="invalid-feedback">
                                            {validationFeedback(
                                                "firstName",
                                                this.state.form
                                            )}
                                        </div>
                                    </Form.Group>
                                    <Form.Group as={Col}>
                                        <Form.Label>Last name</Form.Label>
                                        <Form.Control
                                            maxLength="30"
                                            placeholder="Enter your last name"
                                            onChange={(event) =>
                                                this.inputUpdateHandler(
                                                    event,
                                                    "lastName"
                                                )
                                            }
                                            className={validationClassName(
                                                "lastName",
                                                this.state.form
                                            )}
                                        />
                                        <div class="invalid-feedback">
                                            {validationFeedback(
                                                "lastName",
                                                this.state.form
                                            )}
                                        </div>
                                    </Form.Group>
                                </Form.Row>
                                <Form.Row>
                                    <Form.Group as={Col}>
                                        <Form.Label>E-mail</Form.Label>
                                        <Form.Control
                                            maxLength="50"
                                            name="email"
                                            autoComplete="email"
                                            type="email"
                                            placeholder="Enter your valid e-mail"
                                            onChange={(event) =>
                                                this.inputUpdateHandler(
                                                    event,
                                                    "email"
                                                )
                                            }
                                            className={validationClassName(
                                                "email",
                                                this.state.form
                                            )}
                                        />
                                        <div class="invalid-feedback">
                                            {validationFeedback(
                                                "email",
                                                this.state.form
                                            )}
                                        </div>
                                    </Form.Group>
                                    <Form.Group as={Col}>
                                        <Form.Label>Password</Form.Label>
                                        <Form.Control
                                            type="password"
                                            placeholder="Password"
                                            onChange={(event) =>
                                                this.inputUpdateHandler(
                                                    event,
                                                    "password"
                                                )
                                            }
                                            className={validationClassName(
                                                "password",
                                                this.state.form
                                            )}
                                        />
                                        <div class="invalid-feedback">
                                            {validationFeedback(
                                                "password",
                                                this.state.form
                                            )}
                                        </div>
                                    </Form.Group>
                                </Form.Row>
                                <Form.Row>
                                    <Form.Group as={Col}></Form.Group>
                                    <Form.Group as={Col}>
                                        <Form.Label>Password again</Form.Label>
                                        <Form.Control
                                            type="password"
                                            placeholder="Password again"
                                            onChange={(event) =>
                                                this.inputUpdateHandler(
                                                    event,
                                                    "password_control"
                                                )
                                            }
                                            className={validationClassName(
                                                "password_control",
                                                this.state.form
                                            )}
                                        />
                                        <div class="invalid-feedback">
                                            {validationFeedback(
                                                "password_control",
                                                this.state.form
                                            )}
                                        </div>
                                    </Form.Group>
                                </Form.Row>
                            </div>
                            <br />
                            <div className="window radius">
                                <h5>Address</h5>

                                <Form.Row>
                                    <Form.Group as={Col}>
                                        <Form.Label>Street</Form.Label>
                                        <Form.Control
                                            maxLength="50"
                                            placeholder="Street"
                                            onChange={(event) =>
                                                this.inputUpdateHandler(
                                                    event,
                                                    "street"
                                                )
                                            }
                                            className={validationClassName(
                                                "street",
                                                this.state.form
                                            )}
                                        />
                                        <div class="invalid-feedback">
                                            {validationFeedback(
                                                "street",
                                                this.state.form
                                            )}
                                        </div>
                                    </Form.Group>
                                    <Form.Group as={Col}>
                                        <Form.Label>House number</Form.Label>
                                        <Form.Control
                                            placeholder="House number"
                                            onChange={(event) =>
                                                this.inputUpdateHandler(
                                                    event,
                                                    "houseNumber"
                                                )
                                            }
                                            className={validationClassName(
                                                "houseNumber",
                                                this.state.form
                                            )}
                                        />
                                        <div class="invalid-feedback">
                                            {validationFeedback(
                                                "houseNumber",
                                                this.state.form
                                            )}
                                        </div>
                                    </Form.Group>
                                </Form.Row>

                                <Form.Row>
                                    <Form.Group as={Col}>
                                        <Form.Label>City</Form.Label>
                                        <Form.Control
                                            maxLength="100"
                                            placeholder="City"
                                            onChange={(event) =>
                                                this.inputUpdateHandler(
                                                    event,
                                                    "city"
                                                )
                                            }
                                            className={validationClassName(
                                                "city",
                                                this.state.form
                                            )}
                                        />
                                        <div class="invalid-feedback">
                                            {validationFeedback(
                                                "city",
                                                this.state.form
                                            )}
                                        </div>
                                    </Form.Group>
                                </Form.Row>

                                <Form.Row>
                                    <Form.Group as={Col}></Form.Group>
                                    <Form.Group as={Col}>
                                        <Form.Label>ZIP code</Form.Label>
                                        <Form.Control
                                            type="number"
                                            placeholder="ZIP code"
                                            onChange={(event) =>
                                                this.inputUpdateHandler(
                                                    event,
                                                    "zipCode"
                                                )
                                            }
                                            className={validationClassName(
                                                "zipCode",
                                                this.state.form
                                            )}
                                        />
                                        <div class="invalid-feedback">
                                            {validationFeedback(
                                                "zipCode",
                                                this.state.form
                                            )}
                                        </div>
                                    </Form.Group>
                                    <Form.Group as={Col}>
                                        <Form.Label>Country</Form.Label>
                                        {countryOptions}
                                    </Form.Group>
                                    <Form.Group as={Col}></Form.Group>
                                </Form.Row>
                                <Form.Row>
                                    <Button
                                        className="submit"
                                        variant="primary"
                                        type="submit"
                                    >
                                        Create new account
                                    </Button>
                                </Form.Row>
                            </div>
                        </Form>
                    </Col>
                </Row>
            </Container>
        );
    }
}

export default withRouter(Register);
