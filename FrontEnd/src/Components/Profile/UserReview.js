import React from "react";
import { Container, Row, Col } from "react-bootstrap";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { Link } from "react-router-dom";
import ProgressBar from "react-bootstrap/ProgressBar";
import Image from "react-bootstrap/Image";
import Spinner from "react-bootstrap/Spinner";
import { appContext } from "../../appContext";
class UserReview extends React.Component {
    state = { user: null };
    static contextType = appContext;
    async componentDidMount() {
        this.setState({ user: this.context.user });
    }

    renderRating(rating) {
        let starsElement = [];
        if (rating == 0) {
            return (
                <span style={{ color: "black" }}>Trip has not any review</span>
            );
        }
        for (var i = 1; i <= rating; i++) {
            starsElement.push(<FontAwesomeIcon key={i} icon="star" />);
        }
        if (rating - starsElement.length >= 0.5) {
            starsElement.push(
                <FontAwesomeIcon
                    key={starsElement.length + 1}
                    icon="star-half"
                />
            );
        }
        return starsElement;
    }
    render() {
        if (this.state.user == null) {
            return (
                <Container className="p-5">
                    <Spinner animation="border" role="status">
                        <span className="sr-only">Loading...</span>
                    </Spinner>
                </Container>
            );
        } else {
            return (
                <Container
                    style={{ width: "750px" }}
                    className="trip_reviews window radius"
                >
                    {this.state.user.userReviewDtos.map((review) => (
                        <div className="review" style={{ marginTop: "25px" }}>
                            <Row>
                                <Col className="rev_author" xs={6}>
                                    <FontAwesomeIcon icon="user-alt" />
                                    <span>Peter Testovany </span>
                                    <span>{review.author}</span>
                                    <span className="text-muted">sa</span>
                                </Col>
                                <Col className="rev_rating" xs={6}>
                                    {this.renderRating(review.rating)}
                                </Col>
                            </Row>
                            <Row>
                                <Col>
                                    <p className="note">{review.note}</p>
                                </Col>
                            </Row>
                        </div>
                    ))}
                </Container>
            );
        }
    }
}
export default UserReview;
