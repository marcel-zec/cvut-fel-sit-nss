import React from "react";
import { Form, Col, Button } from "react-bootstrap";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";

class SessionInput extends React.Component {
    constructor(props) {
        super(props);
        let fromDate = this.props.session.from_date
            ? new Date(this.props.session.from_date)
            : null;
        let toDate = this.props.session.to_date
            ? new Date(this.props.session.to_date)
            : null;
        this.state = {
            index: this.props.session.index,
            from_date: fromDate
                ? new Date(
                      fromDate.getTime() + fromDate.getTimezoneOffset() * 60000
                  )
                : null,
            to_date: toDate
                ? new Date(
                      toDate.getTime() + toDate.getTimezoneOffset() * 60000
                  )
                : null,
            capacity: this.props.session.capacity,
        };
        console.log("constructor");
        console.log(this.props);
        console.log(this.state);
    }

    inputUpdateHandler = async (event, nameOfInput) => {
        const newState = { ...this.state };
        newState[nameOfInput] = event.target.value;
        console.log([nameOfInput] + ":" + event.target.value);
        await this.setState(newState);
        console.log(this.state);
        await this.props.onChangeMethod(this.state);
        console.log("update session in session input");
    };

    dateChangeHandler = async (date, stateName) => {
        console.log("before new Date");
        console.log(date);
        let newDate = new Date(date);
        console.log("after new Date");
        console.log(newDate);
        newDate.setTime(
            newDate.getTime() - new Date().getTimezoneOffset() * 60 * 1000
        );
        console.log("after getTimezoneOffset");
        console.log(newDate);
        await this.setState({
            [stateName]: newDate,
        });
        console.log("after setState");
        console.log(this.state);

        await this.props.onChangeMethod(this.state);
        console.log("update date in session input");
    };

    render() {
        return (
            <Form.Row key={this.state.index}>
                <Form.Group
                    as={Col}
                    controlId="formGridName"
                    className="d-flex flex-column"
                >
                    <Form.Label>Date from</Form.Label>
                    <DatePicker
                        className="form-control"
                        selected={this.state.from_date}
                        dateFormat="dd-MM-yyyy"
                        onChange={(date) =>
                            this.dateChangeHandler(date, "from_date")
                        }
                    />
                </Form.Group>

                <Form.Group
                    as={Col}
                    controlId="formGridShortName"
                    className="d-flex flex-column"
                >
                    <Form.Label>Date to</Form.Label>
                    <DatePicker
                        className="form-control"
                        selected={this.state.to_date}
                        dateFormat="dd-MM-yyyy"
                        onChange={(date) =>
                            this.dateChangeHandler(date, "to_date")
                        }
                    />
                </Form.Group>

                <Form.Group as={Col} controlId="formGridShortName">
                    <Form.Label>Capacity</Form.Label>
                    <Form.Control
                        placeholder=""
                        value={this.state.capacity}
                        onChange={(event) =>
                            this.inputUpdateHandler(event, "capacity")
                        }
                    />
                </Form.Group>
                <Form.Group className="d-flex flex-column justify-content-end mb-4">
                    <Button
                        variant="danger"
                        size="sm"
                        onClick={() => this.props.forDeleteSession(this.state)}
                    >
                        <FontAwesomeIcon
                            icon="times"
                            size="lg"
                            onClick={() =>
                                this.props.forDeleteSession(this.state)
                            }
                        />
                    </Button>
                </Form.Group>
            </Form.Row>
        );
    }
}
export default SessionInput;
