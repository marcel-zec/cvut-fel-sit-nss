import React from "react";
import { Container, Card, Form, Row, Col, Button } from "react-bootstrap";
import TripSmall from "./TripSmall";
import CardColumns from "react-bootstrap/CardColumns";
import Spinner from "react-bootstrap/Spinner";
import Cookies from "universal-cookie";
import DatePicker from "react-datepicker";
import RangeSlider from "react-bootstrap-range-slider";
import "react-bootstrap-range-slider/dist/react-bootstrap-range-slider.css";
import TripMedium from "./TripMedium";
import line from "../../../Files/images/topLine.png";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { BASE_API_URL } from "../../../App";

class IndexFilter extends React.Component {
    state = {
        trips: null,
        filter: {
            from: null,
            to: null,
            price: 50,
            words: [],
        },
    };

    componentDidUpdate(prevState) {
        if (prevState.location.search != this.props.location.search) {
            const newState = { ...this.state };
            newState.filter["words"] = this.searchParameter();
            this.setState({ state: newState });
            this.fetchData();
        }
    }

    searchParameter = () => {
        const urlParams = new URLSearchParams(this.props.location.search);
        const parameters = urlParams.get("search");
        return parameters ? parameters.split(" ") : null;
    };

    inputUpdateHandler = (event, inputName) => {
        const newState = { ...this.state.filter };
        if (inputName == "price")
            newState["price"] = Number(event.target.value);
        else if (inputName == "from" || inputName == "to") {
            let newDate = new Date(event);
            console.log("date " + inputName);
            console.log(newDate);
            console.log(
                newDate.getFullYear() +
                    "-" +
                    (newDate.getMonth() + 1) +
                    "-" +
                    newDate.getDate()
            );
            newDate.setTime(
                newDate.getTime() - new Date().getTimezoneOffset() * 60 * 1000
            );
            newState[inputName] = newDate;
        }
        this.setState({ filter: newState });
    };

    searchUrl = () => {
        let filterUrl = BASE_API_URL + "/trip/filter?";
        let filter = false;

        const searchParameters = this.searchParameter();
        console.log("searching");
        console.log(searchParameters);
        console.log(this.state.filter.price);

        if (searchParameters && searchParameters.length > 0) {
            searchParameters.forEach((param) => {
                if (filter) {
                    filterUrl += "&search=" + param;
                } else {
                    filterUrl += "search=" + param;
                }

                filter = true;
            });
        }
        if (this.state.filter.price) {
            if (filter) {
                filterUrl += "&min_price=" + this.state.filter.price;
            } else {
                filterUrl += "min_price=" + this.state.filter.price;
            }
            filter = true;
        }
        if (this.state.filter.from) {
            const date = this.getFormatedDate(this.state.filter.from);
            if (filter) {
                filterUrl += "&from_date=" + date;
            } else {
                filterUrl += "from_date=" + date;
            }
            filter = true;
        }
        if (this.state.filter.to) {
            const date = this.getFormatedDate(this.state.filter.to);
            if (filter) {
                filterUrl += "&to_date=" + date;
            } else {
                filterUrl += "to_date=" + date;
            }
            filter = true;
        }
        return filter ? filterUrl : BASE_API_URL + "/trip";
    };

    getFormatedDate = (date) => {
        const month = ("0" + (date.getMonth() + 1)).slice(-2);
        const day = ("0" + date.getDate()).slice(-2);

        return date.getFullYear() + "-" + month + "-" + day;
    };

    fetchData = async (event = null) => {
        if (event) {
            event.preventDefault();
        }
        const url = this.searchUrl();
        console.log(url);
        await fetch(url, {
            method: "GET",
            mode: "cors",
            credentials: "include",
            headers: {
                "Content-Type": "application/json",
            },
        })
            .then((response) => {
                console.log(response);
                if (response.ok) return response.json();
                else console.error(response.status);
            })
            .then((data) => {
                console.log(data);
                this.setState({ trips: data });
            })
            .catch((error) => {
                console.error(error);
            });
    };

    async componentDidMount() {
        const newState = { ...this.state };
        newState.filter.words = this.searchParameter();
        if (newState.filter.words != this.state.filter.words) {
            this.setState(newState);
        }

        this.fetchData();
    }

    refresh = () => {
        this.setState({
            filter: {
                from: null,
                to: null,
                price: 50,
                words: this.state.filter.words,
            },
        });
    };

    render() {
        if (this.state.trips == null) {
            return (
                <Container className="p-5">
                    <Spinner animation="border" role="status">
                        <span className="sr-only">Loading...</span>
                    </Spinner>
                </Container>
            );
        } else {
            return (
                <Container className="searchTrips mt-5">
                    <div className="topLine">
                        <h4>Search results</h4>
                        <img src={line} />
                    </div>

                    <Row>
                        <Col className="col-md-4 filter">
                            <Card>
                                <Form
                                    onSubmit={(event) => this.fetchData(event)}
                                >
                                    <h5>Filter trips</h5>

                                    <Form.Group>
                                        <Form.Label>Date</Form.Label>
                                        <Card.Body className="d-flex">
                                            <Form.Label>From:</Form.Label>
                                            <DatePicker
                                                className="form-control"
                                                dateFormat="dd. MM. yyyy"
                                                selected={
                                                    this.state.filter.from
                                                }
                                                onChange={(event) =>
                                                    this.inputUpdateHandler(
                                                        event,
                                                        "from"
                                                    )
                                                }
                                            />
                                            <Form.Label>To:</Form.Label>
                                            <DatePicker
                                                className="form-control"
                                                dateFormat="dd. MM. yyyy"
                                                selected={this.state.filter.to}
                                                onChange={(event) =>
                                                    this.inputUpdateHandler(
                                                        event,
                                                        "to"
                                                    )
                                                }
                                            />
                                        </Card.Body>
                                    </Form.Group>
                                    <Form.Group>
                                        <Card.Body className="d-flex">
                                            <Form.Label>Price </Form.Label>
                                            <RangeSlider
                                                value={this.state.filter.price}
                                                min={0}
                                                max={300}
                                                step={50}
                                                tooltip="auto"
                                                tooltipPlacement="top"
                                                onChange={(event) =>
                                                    this.inputUpdateHandler(
                                                        event,
                                                        "price"
                                                    )
                                                }
                                            />
                                        </Card.Body>
                                    </Form.Group>
                                    <Form.Group className="d-flex justify-content-between">
                                        <Button
                                            className="submit"
                                            variant="primary"
                                            type="submit"
                                        >
                                            Filter
                                        </Button>
                                        <Button
                                            variant="danger"
                                            onClick={() => this.refresh()}
                                        >
                                            <FontAwesomeIcon
                                                icon="redo"
                                                size="lg"
                                            />
                                        </Button>
                                    </Form.Group>
                                </Form>
                            </Card>
                        </Col>
                        <Col className="col-md-8">
                            {/*<CardColumns className="d-flex flex-column">*/}
                            {this.state.trips.map((trip) => {
                                return (
                                    <Row>
                                        <TripMedium
                                            key={trip.short_name}
                                            highlightWords={
                                                this.state.filter.words
                                            }
                                            trip={trip}
                                        />
                                    </Row>
                                );
                            })}
                            {/*</CardColumns>*/}
                        </Col>
                    </Row>
                    {/*<CardColumns>
                        {this.state.trips.map((trip) => {
                            return (
                                <TripSmall key={trip.short_name} trip={trip} />
                            );
                        })}
                    </CardColumns>*/}
                </Container>
            );
        }
    }
}

export default IndexFilter;
