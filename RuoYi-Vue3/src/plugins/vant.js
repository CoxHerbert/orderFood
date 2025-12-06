import {
  ActionBar,
  ActionBarButton,
  ActionBarIcon,
  Button,
  Card,
  Cell,
  CellGroup,
  Empty,
  Icon,
  List,
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
  Empty,
  Icon,
  List,
  Loading,
  NavBar,
  Sidebar,
  SidebarItem,
  Stepper,
  Tab,
  Tabs,
  Tag
]

export default function setupVant(app) {
  components.forEach((component) => {
    app.use(component)
  })
  app.use(Toast)
}
