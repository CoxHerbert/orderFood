import {
  ActionBar,
  ActionBarButton,
  ActionBarIcon,
  Button,
  Card,
  Cell,
  CellGroup,
  Dialog,
  Empty,
  Icon,
  Loading,
  NavBar,
  Sidebar,
  SidebarItem,
  Stepper,
  Tab,
  Tabs,
  Tag,
  Toast
} from 'vant'

const components = [
  ActionBar,
  ActionBarButton,
  ActionBarIcon,
  Button,
  Card,
  Cell,
  CellGroup,
  Dialog,
  Empty,
  Icon,
  Loading,
  NavBar,
  Sidebar,
  SidebarItem,
  Stepper,
  Tab,
  Tabs,
  Tag,
  Toast
]

export default function setupVant(app) {
  components.forEach((c) => app.use(c))
}
