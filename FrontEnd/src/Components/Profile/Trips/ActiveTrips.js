import React from "react";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { Card, Button, Col, Row, Image } from "react-bootstrap";
import DatePicker from "react-datepicker";


class ActiveTrips extends React.Component {
    state = {enroll:null,state:null,deposit_was_paid:null};
    async componentDidMount() {
        this.setState({enroll:this.props.trip});
        this.setState({deposit_was_paid:this.props.trip.deposit_was_paid});
        this.setState({state:this.props.trip.state});
    }
    render() {
        //console.log(this.state);
        //console.log(this.props.trip.state);
        this.paided = <span style={{color:"#28a745"}}>Paid <FontAwesomeIcon icon="check-circle"/></span>
        this.buttonToPay = <Button className="submit" onClick={price => this.props.funcToPay(this.state.enroll,this)}>Pay deposit <FontAwesomeIcon icon={"money-bill-alt"}/></Button>;
        this.notPaided = <span style={{color:"#ce3131"}}>Not paid <FontAwesomeIcon icon="minus-circle"/></span>
        this.actionElement = <Button className="cancel" onClick={event => this.props.funcToCancel(this,this.state.enroll)} style={{float:"right"}}>Cancel<FontAwesomeIcon icon={"trash-alt"}/></Button>;
        if  (this.state.enroll == null){
            return "null";
        }else{
            //console.log(this.state);
            return (
                <Card className="mb-3 userTrip window radius">
                    <Card.Body className="d-flex flex-row">
                        <Col xs={2}>
                            <div className="tripImage">
                                <Image src="https://specials-images.forbesimg.com/imageserve/5db15891616a45000704761f/960x0.jpg?fit=scale" rounded/>
                            </div>
                        </Col>
                        <Col xs={3}>
                            <Card.Title className="mb-2 text-muted">
                                Name
                            </Card.Title>
                            <Card.Text>{this.state.enroll.trip.name}</Card.Text>
    
                            <Card.Title className="mb-2 text-muted">
                                Location
                            </Card.Title>
                            <Card.Text>{this.state.enroll.trip.location}</Card.Text>
                        </Col>
                        <Col xs={4}>
                            <Card.Title className="mb-2 text-muted">
                                Trip session
                            </Card.Title>
                            <Card.Text>
                                <DatePicker
                                        className="form-control"
                                        selected={Date.parse(
                                            this.state.enroll.tripSession
                                                .from_date
                                        )}
                                        dateFormat="dd. MM. yyyy"
                                        disabled={true}
                                />
                                <DatePicker
                                        className="form-control"
                                        selected={Date.parse(
                                            this.state.enroll.tripSession
                                                .to_date
                                        )}
                                        dateFormat="dd. MM. yyyy"
                                        disabled={true}
                                />
                            </Card.Text>
    
                            <Card.Title className="mb-2 text-muted">
                                Deposit
                            </Card.Title>
                            <Card.Text>{this.state.enroll.trip.deposit},- {(this.state.deposit_was_paid) ? this.paided : this.notPaided}</Card.Text>
                        </Col>
                        <Col>
                            <Card.Title className="mb-2 text-muted">
                                Added to travel journal on
                            </Card.Title>
                            <Card.Text>
                                <DatePicker
                                        className="form-control"
                                        selected={Date.parse(
                                            this.state.enroll.enrollDate
                                        )}
                                        dateFormat="dd. MM. yyyy, hh:mm"
                                        disabled={true}
                                    />
                            </Card.Text>
                            <Card.Title className="mb-2 text-muted">
                                Action
                            </Card.Title>
                            <Card.Text>
    
                                { (this.state.state!="ACTIVE") ? "Trip was canceled" : ((!this.state.deposit_was_paid) ? this.buttonToPay: '')}
                                { (this.state.state!="ACTIVE") ? "" : this.actionElement}
                            </Card.Text>
                        </Col>
                    </Card.Body>
                </Card>
            );
        }
    }
}

export default ActiveTrips;
