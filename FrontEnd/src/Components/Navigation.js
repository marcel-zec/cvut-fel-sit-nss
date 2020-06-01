import React from "react";
import Navbar from "react-bootstrap/Navbar";
import Nav from "react-bootstrap/Nav";
import NavDropdown from "react-bootstrap/NavDropdown";
import Form from "react-bootstrap/Form";
import FormControl from "react-bootstrap/FormControl";
import Button from "react-bootstrap/Button";
import Container from "react-bootstrap/Container";
import { Row, Col } from "react-bootstrap";
import { NavLink, withRouter } from "react-router-dom";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import logo from "../Files/images/logo.png";
import { appContext } from "../appContext";

class Navigation extends React.Component {
    static contextType = appContext;

    state = {
        search: null,
    };

    searchInputHandler = async (event) => {
        event.target.value.trim() != ""
            ? await this.setState({ search: event.target.value })
            : await this.setState({ search: null });
    };

    searchFormHandler = async (event) => {
        event.preventDefault();
        await this.props.history.replace({
            pathname: "/trips",
            search:
                this.state.search == null
                    ? null
                    : "?search=" + this.state.search,
        });
    };

    render() {
        const ROLE_SUPERUSER = "SUPERUSER";
        const ROLE_ADMIN = "ADMIN";
        const ROLE_USER = "USER";

        let homeButton =
            this.context.user === null ? null : (
                <Col>
                    <Nav.Link>
                        <NavLink to="/profile">
                            {this.context.user.email}
                        </NavLink>
                    </Nav.Link>
                </Col>
            );

        const logoutItem = (
            <NavDropdown.Item>
                <NavLink
                    to="/logout"
                    className="d-flex justify-content-between"
                >
                    <span>Log out</span>
                    <span>
                        <FontAwesomeIcon icon="power-off" />
                    </span>
                </NavLink>
            </NavDropdown.Item>
        );

        const userNavigation = (
            <>
                {this.context.user != null ? (
                    <NavDropdown.Item className="d-flex flex-column align-items-center">
                        <span>
                            {this.context.user.firstName +
                                " " +
                                this.context.user.lastName}
                        </span>
                        <span>
                            {this.context.user.travel_journal
                                ? this.context.user.travel_journal.level +
                                  ". level"
                                : null}
                        </span>
                        <span>
                            {this.context.user.travel_journal
                                ? this.context.user.travel_journal.xp_count +
                                  " XP"
                                : null}
                        </span>
                    </NavDropdown.Item>
                ) : null}

                <NavDropdown.Divider />
                <NavDropdown.Item>
                    <NavLink
                        to="/profile/achievments"
                        className="d-flex justify-content-between"
                    >
                        <span>Moje achievementy</span>
                        <span>
                            <FontAwesomeIcon icon="trophy" className="ml-3" />
                        </span>
                    </NavLink>
                </NavDropdown.Item>
                <NavDropdown.Item>
                    <NavLink
                        to="/profile/trips"
                        className="d-flex justify-content-between"
                    >
                        <span>Moje brigády</span>
                        <span>
                            <FontAwesomeIcon icon="hammer" />
                        </span>
                    </NavLink>
                </NavDropdown.Item>
                <NavDropdown.Item>
                    <NavLink
                        to="/profile/details"
                        className="d-flex justify-content-between"
                    >
                        <span>Nastavenia</span>
                        <span>
                            <FontAwesomeIcon icon="cog" />
                        </span>
                    </NavLink>
                </NavDropdown.Item>
                <NavDropdown.Divider />
                {logoutItem}
            </>
        );

        const adminNavigation = (
            <>
                <NavDropdown.Item>
                    <NavLink to="/close">Uzatvaranie brigád</NavLink>
                </NavDropdown.Item>
                <NavDropdown.Item>
                    <NavLink to="/trip">Brigády</NavLink>
                </NavDropdown.Item>
                <NavDropdown.Item>
                    <NavLink to="/user">Uživatelia</NavLink>
                </NavDropdown.Item>
                <NavDropdown.Item>
                    <NavLink to="/achievement">Achievementy</NavLink>
                </NavDropdown.Item>
                <NavDropdown.Item>
                    <NavLink to="/category">Kategórie</NavLink>
                </NavDropdown.Item>
                <NavDropdown.Divider />
                {logoutItem}
            </>
        );

        const guestNavigation = (
            <>
                <NavDropdown.Item>
                    <NavLink to="/login">
                        Login
                        <FontAwesomeIcon icon="user" />
                    </NavLink>
                </NavDropdown.Item>
                <NavDropdown.Item>
                    <NavLink to="/register">
                        Register
                        <FontAwesomeIcon icon="user" />
                    </NavLink>
                </NavDropdown.Item>
            </>
        );

        let navigationItems = guestNavigation;
        if (this.context.user != null) {
            if (this.context.user.role === ROLE_USER)
                navigationItems = userNavigation;
            else if (this.context.user.role === ROLE_ADMIN)
                navigationItems = adminNavigation;
        }

        return (
            <header>
                <Container className="navigation">
                    <Navbar expand="lg">
                        <Col>
                            <Navbar.Brand>
                                <img src={logo} className="logo" />
                                <NavLink to="/">Travel&Work</NavLink>
                            </Navbar.Brand>
                        </Col>
                        <Navbar.Toggle aria-controls="basic-navbar-nav" />
                        <Navbar.Collapse id="basic-navbar-nav">
                            <Col xs={9}>
                                <Form
                                    inline
                                    onSubmit={(event) =>
                                        this.searchFormHandler(event)
                                    }
                                >
                                    <FormControl
                                        type="text"
                                        placeholder="Search"
                                        className="mr-sm-2"
                                        onChange={(event) =>
                                            this.searchInputHandler(event)
                                        }
                                    />
                                    <Button
                                        variant="outline-success"
                                        type="submit"
                                    >
                                        Search
                                    </Button>
                                </Form>
                            </Col>
                            {homeButton}
                            <Col>
                                <Nav className="mr-auto">
                                    <Col>
                                        <NavDropdown
                                            title=""
                                            id="basic-nav-dropdown"
                                        >
                                            {navigationItems}
                                        </NavDropdown>
                                    </Col>
                                </Nav>
                            </Col>
                        </Navbar.Collapse>
                    </Navbar>
                    <Row></Row>
                </Container>
            </header>
        );
    }
}

export default withRouter(Navigation);
