import React from "react";
import Form from "react-bootstrap/Form";
import { Col, Button, Row, Table } from "react-bootstrap";
import { Container } from "react-bootstrap";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import Spinner from "react-bootstrap/Spinner";
import { Link } from "react-router-dom";
import ButtonInRow from "../../SmartGadgets/ButtonInRow";

class Show extends React.Component {
  state = { users: null };
  async componentDidMount() {
    /*
        const response = await fetch(`http://localhost:8080/trip`);
        const data = await response.json();
        console.log(data);
        this.setState({ trips: data });
        */
  }

  render() {
    if (this.state.user === null) {
      return (
        <Container className="mt-5 p-5">
          <Spinner animation="border" role="status">
            <span className="sr-only">Loading...</span>
          </Spinner>
        </Container>
      );
    } else {
      console.log("else");

      return (
        <Container>
          <Button variant="danger">Block user</Button>
        </Container>
      );
    }
  }
}

export default Show;
